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
import androidx.browser.customtabs.CustomTabsIntent
import java.util.concurrent.atomic.AtomicReference
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import xyz.mcxross.zero.internal.Logger
import xyz.mcxross.zero.model.AuthorizationManagementRequest
import xyz.mcxross.zero.model.AuthorizationRequest
import xyz.mcxross.zero.oauth.AuthorizationManagementUtil
import xyz.mcxross.zero.oauth.AuthorizationService
import xyz.mcxross.zero.oauth.DefaultAuthorizationService

class ZKLoginManagementActivity : AppCompatActivity() {
  private val TAG = "ZKLoginManagementActivity"

  private var authService: AuthorizationService? = null

  private var request: AuthorizationManagementRequest? = null

  private val customTabsIntentAtomicReference = AtomicReference<CustomTabsIntent>()

  private val resultLauncher: ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        Logger.debug(TAG, "onActivityResult OK" + result.data)
        sendResult(Activity.RESULT_OK, result.data)
      } else {
        Logger.debug(TAG, "onActivityResult CANCELLED")
        sendResult(Activity.RESULT_CANCELED, null)
      }
    }

  @Override
  public override fun onCreate(savedInstanceBundle: Bundle?) {
    super.onCreate(savedInstanceBundle)
    if (savedInstanceBundle == null) {
      extractState(intent.extras)
    } else {
      extractState(savedInstanceBundle)
    }

    authService = DefaultAuthorizationService(this)

    if (request != null) {
      val authIntent =
        (authService as DefaultAuthorizationService).getAuthorizationRequestIntent(
          request as AuthorizationRequest,
          customTabsIntentAtomicReference.get()
        )
      resultLauncher.launch(authIntent)
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(KEY_AUTH_REQUEST, Json.encodeToString(serializer(), request))
    outState.putString(KEY_AUTH_REQUEST_TYPE, AuthorizationManagementUtil.requestTypeFor(request))
  }

  private fun extractState(bundle: Bundle?) {

    if (bundle == null) {
      Logger.warn("No stored state - unable to handle response")
      finish()
      return
    }

    val authRequestJson = bundle.getString(KEY_AUTH_REQUEST, null)

    request =
      if (authRequestJson != null)
        AuthorizationManagementUtil.requestFrom(
          authRequestJson,
          AuthorizationManagementUtil.REQUEST_TYPE_AUTHORIZATION
        )
      else null
  }

  private fun sendResult(resultCode: Int, data: Intent?) {
    setResult(resultCode, data)
    finish()
  }

  companion object {

    private const val KEY_AUTH_REQUEST = "authRequest"

    private const val KEY_AUTH_REQUEST_TYPE = "authRequestType"

    fun createStartIntent(
      context: Context,
      request: AuthorizationManagementRequest,
    ): Intent {
      val intent = Intent(context, ZKLoginManagementActivity::class.java)
      intent.putExtra(KEY_AUTH_REQUEST, Json.encodeToString(serializer(), request))
      Logger.debug("createStartIntent", "KEY_AUTH_REQUEST: ${Json.encodeToString(serializer(), request)}")
      return intent
    }
  }
}
