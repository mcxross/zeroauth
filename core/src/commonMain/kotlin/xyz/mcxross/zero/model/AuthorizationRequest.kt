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
package xyz.mcxross.zero.model

import com.eygraber.uri.Uri
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationRequest(
  var clientId: String,
  var redirectUri: String,
  var nonce: String,
) : AuthorizationManagementRequest {

  override val state: String
    get() = TODO("Not yet implemented")

  override fun toUri(): Uri {
    return Uri.Builder()
      .scheme("https")
      .authority("auth.zeropasswords.com")
      .path("/authorize")
      .query("client_id=$clientId&redirect_uri=$redirectUri&nonce=$nonce")
      .build()
  }

  companion object {
    fun fromUri(uri: Uri): AuthorizationRequest? {
      val clientId = uri.getQueryParameter("client_id")
      val redirectUri = uri.getQueryParameter("redirect_uri")
      val nonce = uri.getQueryParameter("nonce")

      return if (clientId != null && redirectUri != null && nonce != null) {
        AuthorizationRequest(clientId, redirectUri, nonce)
      } else {
        null
      }
    }
  }
}
