package xyz.mcxross.zero.oauth

import com.eygraber.uri.Uri
import io.ktor.util.reflect.*
import kotlinx.serialization.json.Json
import xyz.mcxross.zero.model.*

object AuthorizationManagementUtil {
  const val REQUEST_TYPE_AUTHORIZATION = "authorization"
  const val REQUEST_TYPE_END_SESSION = "end_session"

  fun requestTypeFor(request: AuthorizationManagementRequest?): String? {
    if (request is AuthorizationRequest) {
      return REQUEST_TYPE_AUTHORIZATION
    }
    return if (request is EndSessionRequest) {
      REQUEST_TYPE_END_SESSION
    } else null
  }

  fun requestFrom(jsonStr: String, type: String): AuthorizationManagementRequest {
    val json = Json {
      ignoreUnknownKeys = true
      isLenient = true
    }

    if (REQUEST_TYPE_AUTHORIZATION == type) {
      return json.decodeFromString(AuthorizationRequest.serializer(), jsonStr)
    }
    if (REQUEST_TYPE_END_SESSION == type) {
      return json.decodeFromString(EndSessionRequest.serializer(), jsonStr)
    }
    throw IllegalArgumentException(
      "No AuthorizationManagementRequest found matching to this json schema"
    )
  }

  fun responseWith(
    request: AuthorizationManagementRequest?,
    uri: Uri
  ): AuthorizationManagementResponse {
    if (request != null) {
      if (request.instanceOf(AuthorizationRequest::class)) {
        return AuthorizationResponse.fromUri(uri)
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
