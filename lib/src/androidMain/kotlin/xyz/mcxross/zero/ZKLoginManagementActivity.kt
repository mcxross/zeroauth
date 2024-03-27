/*
 * Copyright 2016 McXross. All Rights Reserved.
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.mcxross.zero

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import xyz.mcxross.suiness.OidcProvider
import xyz.mcxross.suiness.generateZkLoginAddress
import xyz.mcxross.zero.exception.ZeroAuthNetworkException
import xyz.mcxross.zero.extension.toAuthorizationReques
import xyz.mcxross.zero.extension.toAuthorizationRequestAsync
import xyz.mcxross.zero.internal.Logger
import xyz.mcxross.zero.model.Apple
import xyz.mcxross.zero.model.AuthorizationManagementRequest
import xyz.mcxross.zero.model.AuthorizationRequest
import xyz.mcxross.zero.model.Ghost
import xyz.mcxross.zero.model.Google
import xyz.mcxross.zero.model.LiveDataProofResponse
import xyz.mcxross.zero.model.Nonce
import xyz.mcxross.zero.model.OpenIDServiceConfiguration
import xyz.mcxross.zero.model.Proof
import xyz.mcxross.zero.model.Provider
import xyz.mcxross.zero.model.Salt
import xyz.mcxross.zero.model.ServiceHolder
import xyz.mcxross.zero.model.Slack
import xyz.mcxross.zero.model.TokenInfo
import xyz.mcxross.zero.model.TokenViewModel
import xyz.mcxross.zero.model.Twitch
import xyz.mcxross.zero.model.ZKLoginRequest
import xyz.mcxross.zero.model.ZKLoginResponse
import xyz.mcxross.zero.oauth.AuthorizationManagementUtil
import xyz.mcxross.zero.oauth.AuthorizationService
import xyz.mcxross.zero.oauth.DefaultAuthorizationService
import xyz.mcxross.zero.rpc.epoch
import xyz.mcxross.zero.service.AndroidDefaultProvingService
import xyz.mcxross.zero.service.AndroidDefaultSaltingService
import xyz.mcxross.zero.service.DefaultSaltingService

class ZKLoginManagementActivity : AppCompatActivity() {
  private val TAG = "ZKLoginManagementActivity"

  private var authorizationManagementStarted = false

  private var authService: AuthorizationService? = null

  private var request: AuthorizationManagementRequest? = null

  private var zkLoginRequest: ZKLoginRequest? = null

  private var data: Intent? = null

  private val resultLauncher: ActivityResultLauncher<Intent> =
      registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
          Activity.RESULT_OK -> {
            data = result.data
            performTokenFetch()
          }
          else -> {
            Logger.debug(TAG, "onActivityResult CANCELLED")
            sendResult(Activity.RESULT_CANCELED, null)
            finish()
          }
        }
      }

  private val tokenViewModel: TokenViewModel by viewModels()

  private val provingService by lazy {
    ServiceHolder.provingService.apply {
      if (this is AndroidDefaultProvingService) {
        this.lifecycleOwner = this@ZKLoginManagementActivity
      }
    }
  }

  @Override
  public override fun onCreate(savedInstanceBundle: Bundle?) {
    super.onCreate(savedInstanceBundle)
    extractState(savedInstanceBundle ?: intent.extras)
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        tokenViewModel.tokenUiState.collect { it ->
          it?.tokenInfo?.let {
            if (zkLoginRequest?.openIDServiceConfiguration?.nonce is Nonce.FromComponents) {
              proveToken(it)
            } else {
              finishZKLogin(it)
            }
          }
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    // If this is the first run of the activity, start the authorization management intent.
    // Note that we do not finish the activity at this point, in order to remain on the back
    // stack underneath the authorization activity.
    if (!authorizationManagementStarted) {
      startAuthorizationManagementIntent()
    }
  }

  private fun startAuthorizationManagementIntent() {
    authService = DefaultAuthorizationService(this)
    (request as? AuthorizationRequest)?.let {
      val authIntent =
          (authService as DefaultAuthorizationService).getAuthorizationRequestIntent(it)
      resultLauncher.launch(authIntent)
    }
    authorizationManagementStarted = true
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    request?.let {
      outState.putString(KEY_AUTH_REQUEST, Json.encodeToString(serializer(), it))
      outState.putString(KEY_AUTH_REQUEST_TYPE, AuthorizationManagementUtil.requestTypeFor(it))
    } ?: Logger.warn("Request is null - state not fully saved")

    zkLoginRequest?.let {
      outState.putString(KEY_ZK_LOGIN_REQUEST, Json.encodeToString(serializer(), it))
    } ?: Logger.warn("ZKLoginRequest is null - state not fully saved")
  }

  private fun extractState(bundle: Bundle?) {
    bundle?.let {
      it.getString(KEY_AUTH_REQUEST, null)?.let { authRequestJson ->
        request =
            AuthorizationManagementUtil.requestFrom(
                authRequestJson, AuthorizationManagementUtil.REQUEST_TYPE_AUTHORIZATION)
      }
          ?: run {
            Logger.warn("Authorization request missing - unable to handle response")
            finish()
          }
    }
        ?: run {
          Logger.warn("No stored state - unable to handle response")
          finish()
        }

    bundle?.let {
      it.getString(KEY_ZK_LOGIN_REQUEST, null)?.let { zkLoginRequestJson ->
        Logger.debug("ZKLoginRequest: $zkLoginRequestJson")
        zkLoginRequest = Json.decodeFromString(serializer(), zkLoginRequestJson)
      }
          ?: run {
            Logger.warn("ZKLoginRequest missing - unable to handle response")
            finish()
          }
    }
  }

  private fun performTokenFetch() {
    val currentRequest = request
    if (currentRequest == null) {
      Logger.warn(TAG, "Request is null, cannot finish ZKLogin")
      // Optionally, handle this scenario, e.g., by calling `finish()` or sending an error result.
      return
    }

    tokenViewModel.fetchToken(
        data?.data?.getQueryParameter("code") ?: "", currentRequest as AuthorizationRequest)
  }

  private fun proveToken(tokenInfo: TokenInfo) {

    Logger.debug("Proving token...")

    val responseWrapper =
        (ServiceHolder.saltingService as? AndroidDefaultSaltingService)?.let {
          provingService?.prove(it.salt, tokenInfo.idToken, zkLoginRequest)
        }

    (responseWrapper as LiveDataProofResponse)
        .liveData
        .observe(
            this,
            Observer { proofResponse ->
              proofResponse?.let {
                Logger.debug("Proof response: $it")
                finishZKLogin(tokenInfo, it)
              }
            })
  }

  private fun finishZKLogin(tokenInfo: TokenInfo, proof: Proof? = null) {

    val zkLoginResponse =
        zkLoginRequest?.let {
          if (it.openIDServiceConfiguration.nonce is Nonce.FromComponents) {
            Logger.debug("Finish zkLogin with Nonce.FromComponents")
            ZKLoginResponse(
                oidc = it.openIDServiceConfiguration,
                generateZkLoginAddress(determineProvider(it.openIDServiceConfiguration.provider), tokenInfo.idToken,
                  (ServiceHolder.saltingService as? AndroidDefaultSaltingService)?.salt ?: "",
                ),
                it.openIDServiceConfiguration.nonce.kp.toString(),
                tokenInfo = tokenInfo,
                salt = Salt(""),
                proof = proof)
          } else {
            Logger.debug("Finish zkLogin with Nonce.FromStr")
            ZKLoginResponse(
                oidc = it.openIDServiceConfiguration,
                tokenInfo = tokenInfo,
                salt = Salt(""),
                proof = proof)
          }
        }

    Logger.debug("ZKLoginResponse done: $zkLoginResponse")

    val intent: Intent =
        Intent().apply {
          putExtra("zkLoginResponse", Json.encodeToString(serializer(), zkLoginResponse))
        }

    Logger.debug("Sending result: $intent")

    sendResult(Activity.RESULT_OK, intent)

    finish()
  }

  private fun determineProvider(provider: Provider): OidcProvider {
    return when (provider) {
      is Ghost -> OidcProvider.GOOGLE
      is Google -> OidcProvider.GOOGLE
      is Apple -> OidcProvider.GOOGLE
      is Twitch -> OidcProvider.TWITCH
      is Slack -> OidcProvider.SLACK
      else -> OidcProvider.GOOGLE
    }
  }

  private fun sendResult(resultCode: Int, data: Intent?) {
    setResult(resultCode, data)
    finish()
  }

  companion object {

    private const val KEY_AUTH_REQUEST = "authRequest"

    private const val KEY_AUTH_REQUEST_TYPE = "authRequestType"

    private const val KEY_ZK_LOGIN_REQUEST = "zkLoginRequest"

    @Throws(ZeroAuthNetworkException::class)
    suspend fun createStartIntent(
        context: Context,
        request: ZKLoginRequest,
    ): Intent {

      return Intent(context, ZKLoginManagementActivity::class.java).apply {
        val authReq = request.toAuthorizationRequestAsync()
        try {
          putExtra(KEY_AUTH_REQUEST, Json.encodeToString(serializer(), authReq))
        } catch (e: ZeroAuthNetworkException) {
          Logger.error("createStartIntent", e)
          throw ZeroAuthNetworkException("Failed to create start intent", e)
        }

        when (request.openIDServiceConfiguration.nonce) {
          is Nonce.FromComponents -> {
            val epoch = epoch(request.openIDServiceConfiguration.nonce.endPoint)

            val updatedZKLoginRequest =
                ZKLoginRequest(
                    OpenIDServiceConfiguration(
                        request.openIDServiceConfiguration.provider,
                        request.openIDServiceConfiguration.clientId,
                        request.openIDServiceConfiguration.redirectUri,
                        Nonce.FromComponents(
                            request.openIDServiceConfiguration.nonce.endPoint,
                            request.openIDServiceConfiguration.nonce.kp,
                            epoch.toInt() + request.openIDServiceConfiguration.nonce.maxEpoch,
                            request.openIDServiceConfiguration.nonce.randomness)),
                    request.saltingService,
                    request.provingService)

            Logger.debug("Updated ZKLoginRequest: $updatedZKLoginRequest")

            putExtra(KEY_ZK_LOGIN_REQUEST, Json.encodeToString(serializer(), updatedZKLoginRequest))
          }
          is Nonce.FromStr -> {
            putExtra(KEY_ZK_LOGIN_REQUEST, Json.encodeToString(serializer(), request))
          }
        }
      }
    }

    fun createStartIntentSync(
        context: Context,
        request: ZKLoginRequest,
    ): Intent {
      require(request.openIDServiceConfiguration.nonce is Nonce.FromStr)
      return Intent(context, ZKLoginManagementActivity::class.java).apply {
        val authReq = request.toAuthorizationReques()
        putExtra(KEY_AUTH_REQUEST, Json.encodeToString(serializer(), authReq))
        putExtra(KEY_ZK_LOGIN_REQUEST, Json.encodeToString(serializer(), request))
      }
    }
  }
}
