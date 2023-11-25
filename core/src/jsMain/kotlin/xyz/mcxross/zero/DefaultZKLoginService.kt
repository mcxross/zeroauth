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

import Ed25519Keypair
import ZKLoginError
import ZKLoginNotifier
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import xyz.mcxross.zero.extension.toAuthorizationRequest
import xyz.mcxross.zero.login.DefaultProvingService
import xyz.mcxross.zero.login.DefaultSaltingService
import xyz.mcxross.zero.model.Ghost
import xyz.mcxross.zero.model.Nonce
import xyz.mcxross.zero.model.ProvingService
import xyz.mcxross.zero.model.SaltingService
import xyz.mcxross.zero.model.ZKLoginRequest
import xyz.mcxross.zero.oauth.DefaultAuthorizationService

@OptIn(ExperimentalJsExport::class, DelicateCoroutinesApi::class)
@JsExport
fun zkLogin(zkLoginRequest: ZKLoginRequest) {
  zkLoginRequest.toAuthorizationRequest {
    val authorizationService = DefaultAuthorizationService()
    GlobalScope.async { authorizationService.performAuthorizationRequest(this, it) }
  }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun continueWithZKLogin(
  saltingService: SaltingService? = DefaultSaltingService(Mysten.MYSTEN_LABS_SALTING_SERVICE_URL),
  provingService: ProvingService? = DefaultProvingService(Mysten.MYSTEN_LABS_PROVING_SERVICE_URL),
  zkLoginNotifier: ZKLoginNotifier
) {

  console.log("Performing zkLogin continuation...")

  val idToken =
    window.location.hash
      .split("#")
      .filter { it.startsWith("id_token") }
      .map { it.split("=")[1] }
      .firstOrNull() ?: ""

  if (idToken.isEmpty() || idToken.isBlank()) {
    console.error("ID Token is empty. Aborting. You need to restart the login process.")
    val dummyReq =
      ZKLoginRequest(
        openIDServiceConfiguration =
          xyz.mcxross.zero.model.OpenIDServiceConfiguration(
            Ghost(),
            "to be determined",
            "to be determined",
            nonce = Nonce.FromPubKey(Ed25519Keypair().getPublicKey())
          )
      )
    zkLoginNotifier.onZKLoginComplete(
      dummyReq,
      null,
      ZKLoginError(
        "ID Token is empty. Aborting. You need to restart the login process.",
        9000,
        Throwable("I could not find the ID Token in the URL.")
      )
    )
    return
  }

  if (saltingService == null) {
    console.log("Salting Service is null. Using default.")
  }

  val defaultSaltingService =
    saltingService ?: DefaultSaltingService(Mysten.MYSTEN_LABS_SALTING_SERVICE_URL)

  console.log("Salting Service endpoint: ${defaultSaltingService.endPoint}")

  if (provingService == null) {
    console.log("Proving Service is null. Using default.")
  }

  val defaultProvingService =
    provingService ?: DefaultProvingService(Mysten.MYSTEN_LABS_PROVING_SERVICE_URL)

  console.log("Proving Service endpoint: ${defaultProvingService.endPoint}")
}

@OptIn(ExperimentalJsExport::class)
@JsExport
actual open class DefaultZKLoginService : ZKLoginService {

  override fun zkLogin(zkLoginRequest: ZKLoginRequest) = xyz.mcxross.zero.zkLogin(zkLoginRequest)
}
