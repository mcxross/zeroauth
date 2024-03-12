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

import com.eygraber.uri.Uri
import io.ktor.util.reflect.*
import kotlinx.serialization.json.Json
import xyz.mcxross.zero.model.*

object AuthorizationManagementUtil {
  const val REQUEST_TYPE_AUTHORIZATION = "authorization"
  const val REQUEST_TYPE_END_SESSION = "end_session"

  fun requestTypeFor(request: AuthorizationManagementRequest?): String? =
    when (request) {
      is AuthorizationRequest -> {
        REQUEST_TYPE_AUTHORIZATION
      }
      is EndSessionRequest -> {
        REQUEST_TYPE_END_SESSION
      }
      else -> {
        null
      }
    }

  /**
   * Reads an authorization request from a JSON string representation produced by either {@link
   * AuthorizationRequest#jsonSerialize()} or {@link EndSessionRequest#jsonSerialize()}.
   *
   * @throws IllegalArgumentException if the provided JSON does not match the expected structure.
   */
  fun requestFrom(jsonStr: String, type: String): AuthorizationManagementRequest {
    val json = Json {
      ignoreUnknownKeys = true
      isLenient = true
    }

    return when (type) {
      REQUEST_TYPE_AUTHORIZATION -> {
        json.decodeFromString(AuthorizationRequest.serializer(), jsonStr)
      }
      REQUEST_TYPE_END_SESSION -> {
        json.decodeFromString(EndSessionRequest.serializer(), jsonStr)
      }
      else -> {
        throw IllegalArgumentException(
          "No AuthorizationManagementRequest found matching to this json schema",
        )
      }
    }
  }

  /**
   * Builds an AuthorizationManagementResponse from {@link AuthorizationManagementRequest} and
   * {@link Uri}
   */
  fun responseWith(
    request: AuthorizationManagementRequest?,
    uri: Uri
  ): AuthorizationManagementResponse {
    if (request != null) {
      if (request.instanceOf(AuthorizationRequest::class)) {
        return AuthorizationResponse.fromUri(uri, request as AuthorizationRequest)
      }
    }

    if (request != null) {
      if (request.instanceOf(EndSessionRequest::class)) {
        return EndSessionResponse.fromUri(uri)
      }
    }
    throw IllegalArgumentException("Malformed request or uri")
  }
}
