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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.mcxross.zero.oauth.AdditionalParamsProcessor
import xyz.mcxross.zero.util.getQueryParameterAsLong

@Serializable
data class AuthorizationResponse(
  @SerialName("request") val request: AuthorizationRequest?,
  @SerialName("token_type") val tokenType: String?,
  @SerialName("code") val authorizationCode: String?,
  @SerialName("access_token") val accessToken: String?,
  @SerialName("expires_at") val accessTokenExpirationTime: Long?,
  @SerialName("id_token") val idToken: String?,
  @SerialName("scope") val scope: String?,
  @SerialName("additional_parameters") val additionalParameters: Map<String, String> = emptyMap(),
  override val state: String = "",
) : AuthorizationManagementResponse {
  companion object {

    /** The extra string used to store an [AuthorizationResponse] in an intent by [.toIntent]. */
    const val EXTRA_RESPONSE: String = "xyz.mcxross.zero.model.AuthorizationResponse"

    const val KEY_EXPIRES_IN = "expires_in"
    fun fromUri(uri: Uri): AuthorizationResponse =
      AuthorizationResponse(
        request = AuthorizationRequest.fromUri(uri),
        tokenType = uri.getQueryParameter("token_type"),
        authorizationCode = uri.getQueryParameter("code"),
        accessToken = uri.getQueryParameter("access_token"),
        accessTokenExpirationTime = uri.getQueryParameterAsLong(uri, KEY_EXPIRES_IN),
        idToken = uri.getQueryParameter("id_token"),
        scope = uri.getQueryParameter("scope"),
      )
  }
}
