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
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import xyz.mcxross.zero.extension.toAuthorizationRequest
import xyz.mcxross.zero.internal.Logger
import xyz.mcxross.zero.login.AndroidDefaultProvingService
import xyz.mcxross.zero.login.AndroidDefaultSaltingService
import xyz.mcxross.zero.model.AuthorizationManagementRequest
import xyz.mcxross.zero.model.AuthorizationRequest
import xyz.mcxross.zero.model.DefaultProofRequest
import xyz.mcxross.zero.model.DefaultSaltRequest
import xyz.mcxross.zero.model.DefaultSaltResponse
import xyz.mcxross.zero.model.LiveDataProofResponse
import xyz.mcxross.zero.model.LiveDataSaltResponse
import xyz.mcxross.zero.model.ProofResponse
import xyz.mcxross.zero.model.ProvingResponseWrapper
import xyz.mcxross.zero.model.SaltResponse
import xyz.mcxross.zero.model.SaltResponseWrapper
import xyz.mcxross.zero.model.ServiceHolder
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
          Logger.debug(TAG, "onActivityResult OK: ${result.data}")
          data = result.data
          handleZKLogin(data)
        }
        else -> {
          Logger.debug(TAG, "onActivityResult CANCELLED")
          sendResult(Activity.RESULT_CANCELED, null)
          finish()
        }
      }
    }

  @Override
  public override fun onCreate(savedInstanceBundle: Bundle?) {
    super.onCreate(savedInstanceBundle)
    extractState(savedInstanceBundle ?: intent.extras)
  }

  override fun onResume() {
    super.onResume()
    // If this is the first run of the activity, start the authorization management intent.
    // Note that we do not finish the activity at this point, in order to remain on the back
    // stack underneath the authorization activity.
    if (authorizationManagementStarted) {
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
            authRequestJson,
            AuthorizationManagementUtil.REQUEST_TYPE_AUTHORIZATION
          )
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

  private fun handleZKLogin(data: Intent?) {
    data
      ?: run {
        Logger.warn(TAG, "data is null, terminating")
        sendResult(RESULT_CANCELED, null)
        finish()
        return
      }
    val salt = handleSalting()
    val proof = handleProving(salt)
    finishZKLogin(salt, proof)
  }

  private fun handleSalting(): SaltResponse {
    val saltingService = ServiceHolder.saltingService
    val saltResponseWrapper: SaltResponseWrapper?
    var actualSalt: SaltResponse? = null

    if (saltingService == null) {
      Logger.warn(TAG, "SaltingService is null, terminating with data: $data")
      sendResult(Constant.SALTING_FAIL_SERVICE_NOT_DEFINED, data)
      // We can now terminate the activity
      finish()
    }

    when (saltingService) {
      is AndroidDefaultSaltingService -> {
        Logger.debug(TAG, "SaltingService is AndroidDefaultSaltingService, performing salting")
        val androidDefaultSaltingService =
          ServiceHolder.saltingService as AndroidDefaultSaltingService
        // We need this to launch coroutines
        androidDefaultSaltingService.lifecycleOwner = this

        saltResponseWrapper = androidDefaultSaltingService.salt(DefaultSaltRequest(""))
      }
      else -> {
        // Just call the salt method
        saltResponseWrapper = saltingService?.salt(DefaultSaltRequest(""))
      }
    }

    if (saltResponseWrapper is LiveDataSaltResponse) {
      Logger.debug(TAG, "SaltResponseWrapper is LiveDataSaltResponse, succeeding")
      saltResponseWrapper.liveData.observe(this) { salt ->
        Logger.debug(TAG, "SaltResponseWrapper LiveData changed, succeeding")
        actualSalt = salt ?: return@observe
      }
    } else {
      // Just a normal SaltResponseWrapper
      Logger.debug(TAG, "SaltResponseWrapper is not LiveDataSaltResponse, succeeding")
      if (saltResponseWrapper != null) {
        actualSalt = saltResponseWrapper.saltResponse
      } else {
        // TODO: Handle this. User implemented SaltingService is returning null
      }
    }
    return actualSalt!!
  }

  private fun handleProving(salt: SaltResponse): ProofResponse {
    // Now we make a request to the proving service
    val provingService = ServiceHolder.provingService
    val provingResponseWrapper: ProvingResponseWrapper?
    var proofResponse: ProofResponse? = null

    if (provingService == null) {
      Logger.warn(TAG, "ProvingService is null, terminating with data: $data")
      sendResult(Constant.PROVING_SERVICE_FAIL, data)
      // We can now terminate the activity
      finish()
    }

    when (provingService) {
      is AndroidDefaultProvingService -> {
        Logger.debug(TAG, "ProvingService is AndroidDefaultProvingService, performing proving")
        val androidDefaultProvingService =
          ServiceHolder.provingService as AndroidDefaultProvingService
        // We need this to launch coroutines
        androidDefaultProvingService.lifecycleOwner = this
        provingResponseWrapper =
          provingService.prove(
            DefaultProofRequest("", "", 2, "", (salt as DefaultSaltResponse).salt, "")
          )
      }
      else -> {
        // Just call the salt method
        provingResponseWrapper = provingService?.prove(DefaultProofRequest("", "", 2, "", "", ""))
      }
    }

    if (provingResponseWrapper is LiveDataProofResponse) {
      Logger.debug(TAG, "ProvingResponseWrapper is LiveDataProofResponse, succeeding")
      provingResponseWrapper.liveData.observe(this) { proof ->
        Logger.debug(TAG, "ProvingResponseWrapper LiveData changed, succeeding")
        proofResponse = proof ?: return@observe
      }
    } else {
      Logger.warn(TAG, "ProvingResponseWrapper is not LiveDataProofResponse, failing")
      sendResult(Constant.PROVING_SERVICE_FAIL, null)
      finish()
    }
    return proofResponse!!
  }

  private fun finishZKLogin(saltResponseWrapper: SaltResponse, proof: ProofResponse) {
    val currentRequest = request
    if (currentRequest == null) {
      Logger.warn(TAG, "Request is null, cannot finish ZKLogin")
      // Optionally, handle this scenario, e.g., by calling `finish()` or sending an error result.
      return
    }

    val zkLoginResponse =
      ZKLoginResponse(
        request = currentRequest,
        saltResponse = saltResponseWrapper,
        proofResponse = proof
      )

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

    fun createStartIntent(
      context: Context,
      request: ZKLoginRequest,
    ): Intent {
      val authorizationRequest = request.toAuthorizationRequest()

      return Intent(context, ZKLoginManagementActivity::class.java).apply {
        putExtra(KEY_AUTH_REQUEST, Json.encodeToString(serializer(), authorizationRequest))
        Logger.debug(
          "createStartIntent",
          "$KEY_AUTH_REQUEST: ${Json.encodeToString(serializer(), request)}"
        )
      }
    }
  }
}
