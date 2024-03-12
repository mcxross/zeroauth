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
package xyz.mcxross.zero.oauth

import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.w3c.dom.Location
import xyz.mcxross.zero.LocalStorageBackend
import xyz.mcxross.zero.model.AuthorizationRequest
import xyz.mcxross.zero.model.toUri

actual class DefaultAuthorizationService : AuthorizationService {

  private val storageBackend = LocalStorageBackend()

  private val locationLike: Location = window.location

  suspend fun performAuthorizationRequest(
    scope: CoroutineScope,
    authorizationRequest: AuthorizationRequest
  ) {
    val handle = "smala"

    val deferreds =
      listOf(
        scope.async { storageBackend.setItem(AUTHORIZATION_REQUEST_HANDLE_KEY, handle) },
        scope.async {
          storageBackend.setItem(
            authorizationRequestKey(handle),
            Json.encodeToString(serializer(), authorizationRequest)
          )
        },
        scope.async {
          storageBackend.setItem(
            authorizationServiceConfigurationKey(handle),
            Json.encodeToString(serializer(), authorizationRequest.configuration)
          )
        }
      )

    deferreds.awaitAll()

    val url = authorizationRequest.toUri().toString()

    console.log("Making a request to ", authorizationRequest, url)

    locationLike.assign(url)
  }

  private fun authorizationRequestKey(handle: String): String {
    return "${handle}_appauth_authorization_request"
  }

  private fun authorizationServiceConfigurationKey(handle: String): String {
    return "${handle}_appauth_authorization_service_configuration"
  }

  companion object {
    private const val AUTHORIZATION_REQUEST_HANDLE_KEY = "appauth_current_authorization_request"
  }
}
