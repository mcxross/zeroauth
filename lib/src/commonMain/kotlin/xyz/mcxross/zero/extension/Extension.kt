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
package xyz.mcxross.zero.extension

import xyz.mcxross.zero.model.AuthorizationRequest
import xyz.mcxross.zero.model.AuthorizationServiceConfiguration
import xyz.mcxross.zero.model.Nonce
import xyz.mcxross.zero.model.Scope
import xyz.mcxross.zero.model.ZKLoginRequest

fun ZKLoginRequest.toAuthorizationRequest(): AuthorizationRequest {
  val authServiceConfig =
      with(openIDServiceConfiguration.provider) {
        AuthorizationServiceConfiguration(
            authorizationEndpoint, tokenEndpoint, revocationEndpoint, registrationEndpoint)
      }

  return AuthorizationRequest(
      configuration = authServiceConfig,
      clientId = openIDServiceConfiguration.clientId,
      responseType = "id_token",
      redirectUri = openIDServiceConfiguration.redirectUri,
      scope = Scope.OpenID,
      nonce = openIDServiceConfiguration.nonce.toString(),
  )
}

fun ZKLoginRequest.toAuthorizationRequest(callback: (AuthorizationRequest) -> Unit) {
  determineNonce(openIDServiceConfiguration.nonce, endPoint) { nonce ->
    val authServiceConfig =
        with(openIDServiceConfiguration.provider) {
          AuthorizationServiceConfiguration(
              authorizationEndpoint, tokenEndpoint, revocationEndpoint, registrationEndpoint)
        }
    callback(
        AuthorizationRequest(
            configuration = authServiceConfig,
            clientId = openIDServiceConfiguration.clientId,
            responseType = "id_token",
            redirectUri = openIDServiceConfiguration.redirectUri,
            scope = Scope.OpenID,
            nonce = nonce,
        ))
  }
}

suspend fun ZKLoginRequest.toAuthorizationRequestAsync(): AuthorizationRequest {
  val authServiceConfig =
      with(openIDServiceConfiguration.provider) {
        AuthorizationServiceConfiguration(
            authorizationEndpoint, tokenEndpoint, revocationEndpoint, registrationEndpoint)
      }
  return AuthorizationRequest(
      configuration = authServiceConfig,
      clientId = openIDServiceConfiguration.clientId,
      // TODO: a quick fix for native client (Android) to use code flow
      responseType = "code",
      redirectUri = openIDServiceConfiguration.redirectUri,
      scope = Scope.OpenID,
      nonce = openIDServiceConfiguration.nonce.generateAsync(endPoint))
}

private fun determineNonce(nonce: Nonce, url: String, callback: (String) -> Unit) =
    nonce.generate(url, callback)
