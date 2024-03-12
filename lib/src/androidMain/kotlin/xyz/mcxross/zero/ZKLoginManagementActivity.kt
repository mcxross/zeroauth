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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import xyz.mcxross.zero.extension.toAuthorizationRequestAsync
import xyz.mcxross.zero.internal.Logger
import xyz.mcxross.zero.model.AuthorizationManagementRequest
import xyz.mcxross.zero.model.AuthorizationRequest
import xyz.mcxross.zero.model.TokenInfo
import xyz.mcxross.zero.model.TokenViewModel
import xyz.mcxross.zero.model.ZKLoginRequest
import xyz.mcxross.zero.model.ZKLoginResponse
import xyz.mcxross.zero.oauth.AuthorizationManagementUtil
import xyz.mcxross.zero.oauth.AuthorizationService
import xyz.mcxross.zero.oauth.DefaultAuthorizationService

class ZKLoginManagementActivity : AppCompatActivity() {
  private val TAG = "ZKLoginManagementActivity"

  private var authorizationManagementStarted = false

  private var authService: AuthorizationService? = null

  private var request: AuthorizationManagementRequest? = null

  private var data: Intent? = null

  private val resultLauncher: ActivityResultLauncher<Intent> =
      registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
          Activity.RESULT_OK -> {
            data = result.data
            Logger.debug("Code: ${data?.data?.getQueryParameter("code")}")
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

  @Override
  public override fun onCreate(savedInstanceBundle: Bundle?) {
    super.onCreate(savedInstanceBundle)
    extractState(savedInstanceBundle ?: intent.extras)
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        tokenViewModel.tokenUiState.collect { it -> it?.tokenInfo?.let { finishZKLogin(it) } }
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

  private fun finishZKLogin(tokenInfo: TokenInfo) {
    val zkLoginResponse =
        ZKLoginResponse(request = request as AuthorizationRequest, tokenInfo = tokenInfo)

    val intent: Intent =
        Intent().apply {
          putExtra("zkLoginResponse", Json.encodeToString(serializer(), zkLoginResponse))
        }
    sendResult(Activity.RESULT_OK, intent)
    finish()
  }

  private fun sendResult(resultCode: Int, data: Intent?) {
    setResult(resultCode, data)
    finish()
  }

  companion object {

    private const val KEY_AUTH_REQUEST = "authRequest"

    private const val KEY_AUTH_REQUEST_TYPE = "authRequestType"

    private const val KEY_ZK_LOGIN_REQUEST = "zkLoginRequest"

    suspend fun createStartIntent(
        context: Context,
        request: ZKLoginRequest,
    ): Intent {

      return Intent(context, ZKLoginManagementActivity::class.java).apply {
        putExtra(
            KEY_AUTH_REQUEST,
            Json.encodeToString(serializer(), request.toAuthorizationRequestAsync()))
        Logger.debug(
            "createStartIntent${Json.encodeToString(serializer(), request.toAuthorizationRequestAsync())}")
      }
    }
  }
}
