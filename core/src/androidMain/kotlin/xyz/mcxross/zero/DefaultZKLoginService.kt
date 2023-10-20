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

import android.content.Context
import android.content.Intent
import xyz.mcxross.zero.extension.toUri
import xyz.mcxross.zero.model.AuthorizationRequest
import xyz.mcxross.zero.model.AuthorizationServiceConfiguration
import xyz.mcxross.zero.model.Ghost
import xyz.mcxross.zero.model.Provider
import xyz.mcxross.zero.model.ZKLoginRequest

fun zkLoginIntent(context: Context, zkLoginRequest: ZKLoginRequest): Intent {

  return with(zkLoginRequest) {
    val authServiceConfig =
      AuthorizationServiceConfiguration(
        provider.authorizationEndpoint.toUri(),
        provider.tokenEndpoint.toUri(),
        provider.revocationEndpoint?.toUri(),
        provider.registrationEndpoint?.toUri()
      )

    val authRequest =
      AuthorizationRequest(
        configuration = authServiceConfig,
        clientId = clientId,
        redirectUri = redirectUri.toUri(),
        nonce = nonce
      )

    ZKLoginManagementActivity.createStartIntent(context, authRequest)
  }
}

actual class DefaultZKLoginService : ZKLoginService {

  fun zkLoginIntent(context: Context, zkLoginRequest: ZKLoginRequest): Intent =
    xyz.mcxross.zero.zkLoginIntent(context, zkLoginRequest)

  fun zkLoginIntent(context: Context, configAction: ZKLoginRequestConfig.() -> Unit): Intent {
    val requestWrapper = ZKLoginRequestConfig().apply(configAction)
    val request =
      with(requestWrapper) {
        ZKLoginRequest(
          clientId = clientId,
          redirectUri = redirectUri,
          nonce = nonce,
          provider = provider
        )
      }
    return zkLoginIntent(context, request)
  }

  actual override fun performLogin() {
    TODO()
  }
}

class ZKLoginRequestConfig {
  var clientId: String = ""
  var redirectUri: String = ""
  var nonce: String = ""
  var provider: Provider = Ghost()
}
