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
import xyz.mcxross.zero.login.AndroidDefaultSaltingService
import xyz.mcxross.zero.login.DefaultProvingService
import xyz.mcxross.zero.login.DefaultSaltingService
import xyz.mcxross.zero.model.Ghost
import xyz.mcxross.zero.model.Nonce
import xyz.mcxross.zero.model.OpenIDServiceConfiguration
import xyz.mcxross.zero.model.Provider
import xyz.mcxross.zero.model.ProvingService
import xyz.mcxross.zero.model.SaltingService
import xyz.mcxross.zero.model.ServiceHolder
import xyz.mcxross.zero.model.ZKLoginRequest

fun zkLogin(context: Context, zkLoginRequest: ZKLoginRequest): Intent {
  return ServiceHolder.apply {
      saltingService =
        when (zkLoginRequest.saltingService) {
          is DefaultSaltingService -> // Switch to Android-specific implementation
          AndroidDefaultSaltingService(zkLoginRequest.saltingService.endPoint)
          else -> // Okay, we hope the user knows what they're doing, proceed as normal
          zkLoginRequest.saltingService
        }
      provingService = zkLoginRequest.provingService
    }
    .run { ZKLoginManagementActivity.createStartIntent(context, zkLoginRequest) }
}

fun zkLogin(context: Context, configAction: ZKLoginRequestConfig.() -> Unit): Intent {
  val requestWrapper = ZKLoginRequestConfig().apply(configAction)
  val request =
    with(requestWrapper) {
      ZKLoginRequest(
        openIDServiceConfiguration =
          OpenIDServiceConfiguration(
            clientId = clientId,
            redirectUri = redirectUri,
            nonce = nonce,
            provider = provider,
          ),
        saltingService = saltingService,
        provingService = provingService
      )
    }
  return zkLogin(context, request)
}

actual open class DefaultZKLoginService(private val context: Context) : ZKLoginService {
  override fun zkLogin(zkLoginRequest: ZKLoginRequest): Intent = zkLogin(context, zkLoginRequest)

  fun zkLogin(configAction: ZKLoginRequestConfig.() -> Unit): Intent {
    val requestWrapper = ZKLoginRequestConfig().apply(configAction)
    val request =
      with(requestWrapper) {
        ZKLoginRequest(
          openIDServiceConfiguration =
            OpenIDServiceConfiguration(
              clientId = clientId,
              redirectUri = redirectUri,
              nonce = nonce,
              provider = provider,
            ),
          saltingService = saltingService,
          provingService = provingService
        )
      }
    return zkLogin(context, request)
  }
}

class ZKLoginRequestConfig {
  var provider: Provider = Ghost()
  var clientId: String = ""
  var redirectUri: String = ""
  var nonce: Nonce = Nonce.FromPubKey("")
  var saltingService: SaltingService =
    AndroidDefaultSaltingService(Mysten.MYSTEN_LABS_SALTING_SERVICE_URL)
  var provingService: ProvingService = DefaultProvingService(Mysten.MYSTEN_LABS_PROVING_SERVICE_URL)
}
