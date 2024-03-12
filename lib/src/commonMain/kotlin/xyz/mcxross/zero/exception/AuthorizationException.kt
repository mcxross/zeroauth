/*
 * Copyright 2016 McXross. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.mcxross.zero.exception

import com.eygraber.uri.Uri
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import xyz.mcxross.zero.exception.AuthorizationException.AuthorizationRequestErrors.ACCESS_DENIED
import xyz.mcxross.zero.exception.AuthorizationException.AuthorizationRequestErrors.INVALID_REQUEST
import xyz.mcxross.zero.exception.AuthorizationException.AuthorizationRequestErrors.INVALID_SCOPE
import xyz.mcxross.zero.exception.AuthorizationException.AuthorizationRequestErrors.OTHER
import xyz.mcxross.zero.exception.AuthorizationException.AuthorizationRequestErrors.UNAUTHORIZED_CLIENT
import xyz.mcxross.zero.exception.AuthorizationException.AuthorizationRequestErrors.UNSUPPORTED_RESPONSE_TYPE
import xyz.mcxross.zero.exception.AuthorizationException.AuthorizationRequestErrors.exceptionMapByString
import xyz.mcxross.zero.serializer.ThrowableSerializer

@Serializable
data class AuthorizationException(
  val type: Int,
  val code: Int,
  val error: String?,
  val errorDescription: String?,
  val errorUri: Uri?,
  @Serializable(with = ThrowableSerializer::class) val rootCause: Throwable?
) : Exception(errorDescription, rootCause) {

  companion object {
    /**
     * The extra string that used to store an [AuthorizationException] in an intent by [.toIntent].
     */
    const val EXTRA_EXCEPTION = "net.openid.appauth.AuthorizationException"

    /**
     * The OAuth2 parameter used to indicate the type of error during an authorization or token
     * request.
     *
     * @see "The OAuth 2.0 Authorization Framework
     * @see "The OAuth 2.0 Authorization Framework"
     */
    const val PARAM_ERROR = "error"

    /**
     * The OAuth2 parameter used to provide a human-readable description of the error which
     * occurred.
     *
     * @see "The OAuth 2.0 Authorization Framework
     * @see "The OAuth 2.0 Authorization Framework"
     */
    const val PARAM_ERROR_DESCRIPTION = "error_description"

    /**
     * The OAuth2 parameter used to provide a URI to a human-readable page which describes the
     * error.
     *
     * @see "The OAuth 2.0 Authorization Framework
     * @see "The OAuth 2.0 Authorization Framework"
     */
    const val PARAM_ERROR_URI = "error_uri"

    /** The error type used for all errors that are not specific to OAuth related responses. */
    const val TYPE_GENERAL_ERROR = 0

    /**
     * The error type for OAuth specific errors on the authorization endpoint. This error type is
     * used when the server responds to an authorization request with an explicit OAuth error, as
     * defined by [the OAuth2 specification, section 4.1.2.1](
     * https://tools.ietf.org/html/rfc6749#section-4.1.2.1). If the authorization response is
     * invalid and not explicitly an error response, another error type will be used.
     *
     * @see "The OAuth 2.0 Authorization Framework
     */
    const val TYPE_OAUTH_AUTHORIZATION_ERROR = 1

    /**
     * The error type for OAuth specific errors on the token endpoint. This error type is used when
     * the server responds with HTTP 400 and an OAuth error, as defined by
     * [the OAuth2 specification, section 5.2](https://tools.ietf.org/html/rfc6749#section-5.2). If
     * an HTTP 400 response does not parse as an OAuth error (i.e. no 'error' field is present or
     * the JSON is invalid), another error domain will be used.
     *
     * @see "The OAuth 2.0 Authorization Framework"
     */
    const val TYPE_OAUTH_TOKEN_ERROR = 2

    /** The error type for authorization errors encountered out of band on the resource server. */
    const val TYPE_RESOURCE_SERVER_AUTHORIZATION_ERROR = 3

    /** The error type for OAuth specific errors on the registration endpoint. */
    const val TYPE_OAUTH_REGISTRATION_ERROR = 4

    private val STRING_TO_EXCEPTION: Map<String, AuthorizationException> =
      exceptionMapByString(
        INVALID_REQUEST,
        UNAUTHORIZED_CLIENT,
        ACCESS_DENIED,
        UNSUPPORTED_RESPONSE_TYPE,
        INVALID_SCOPE,
        /*  SERVER_ERROR,
        LocationProvider.TEMPORARILY_UNAVAILABLE,
        SipErrorCode.CLIENT_ERROR,*/
        OTHER
      )

    fun byString(error: String?): AuthorizationException {
      val ex: AuthorizationException? = STRING_TO_EXCEPTION[error]
      return ex ?: OTHER
    }

    fun authEx(code: Int, error: String?): AuthorizationException {
      return AuthorizationException(TYPE_OAUTH_AUTHORIZATION_ERROR, code, error, null, null, null)
    }

    private fun generalEx(code: Int, errorDescription: String?): AuthorizationException {
      return AuthorizationException(TYPE_GENERAL_ERROR, code, null, errorDescription, null, null)
    }

    /**
     * Creates an exception based on one of the existing values defined in [GeneralErrors],
     * [AuthorizationRequestErrors] or [TokenRequestErrors], providing a root cause.
     */
    fun fromTemplate(ex: AuthorizationException, rootCause: Throwable?): AuthorizationException {
      return AuthorizationException(
        ex.type,
        ex.code,
        ex.error,
        ex.errorDescription,
        ex.errorUri,
        rootCause
      )
    }

    fun toJsonString(exception: AuthorizationException): String {
      return Json.encodeToString(exception)
    }

    fun fromJsonString(jsonStr: String): AuthorizationException {
      return Json.decodeFromString(jsonStr)
    }

    /** Creates an exception from an OAuth redirect URI that describes an authorization failure. */
    fun fromOAuthRedirect(redirectUri: Uri): AuthorizationException {
      val error: String? = redirectUri.getQueryParameter(PARAM_ERROR)
      val errorDescription: String? = redirectUri.getQueryParameter(PARAM_ERROR_DESCRIPTION)
      val errorUri: String? = redirectUri.getQueryParameter(PARAM_ERROR_URI)
      val (type, code, _, errorDescription1, errorUri1) = AuthorizationRequestErrors.byString(error)
      return AuthorizationException(
        type,
        code,
        error,
        errorDescription ?: errorDescription1,
        if (errorUri != null) Uri.parse(errorUri) else errorUri1,
        null
      )
    }
  }


  /**
   * Error codes specific to AppAuth for Android, rather than those defined in the OAuth2 and
   * OpenID specifications.
   */
  object GeneralErrors {
    // codes in this group should be between 0-999
    /**
     * Indicates a problem parsing an OpenID Connect Service Discovery document.
     */
    val INVALID_DISCOVERY_DOCUMENT = generalEx(0, "Invalid discovery document")

    /**
     * Indicates the user manually canceled the OAuth authorization code flow.
     */
    val USER_CANCELED_AUTH_FLOW = generalEx(1, "User cancelled flow")

    /**
     * Indicates an OAuth authorization flow was programmatically cancelled.
     */
    val PROGRAM_CANCELED_AUTH_FLOW = generalEx(2, "Flow cancelled programmatically")

    /**
     * Indicates a network error occurred.
     */
    val NETWORK_ERROR = generalEx(3, "Network error")

    /**
     * Indicates a server error occurred.
     */
    val SERVER_ERROR = generalEx(4, "Server error")

    /**
     * Indicates a problem occurred deserializing JSON.
     */
    val JSON_DESERIALIZATION_ERROR = generalEx(5, "JSON deserialization error")

    /**
     * Indicates a problem occurred constructing a [token response][TokenResponse] object
     * from the JSON provided by the server.
     */
    val TOKEN_RESPONSE_CONSTRUCTION_ERROR = generalEx(6, "Token response construction error")

    /**
     * Indicates a problem parsing an OpenID Connect Registration Response.
     */
    val INVALID_REGISTRATION_RESPONSE = generalEx(7, "Invalid registration response")

    /**
     * Indicates that a received ID token could not be parsed
     */
    val ID_TOKEN_PARSING_ERROR = generalEx(8, "Unable to parse ID Token")

    /**
     * Indicates that a received ID token is invalid
     */
    val ID_TOKEN_VALIDATION_ERROR = generalEx(9, "Invalid ID Token")
  }


  /**
   * Error codes related to failed authorization requests.
   *
   * @see "The OAuth 2.0 Authorization Framework
   */
  object AuthorizationRequestErrors {
    // codes in this group should be between 1000-1999
    /** An `invalid_request` OAuth2 error response. */
    val INVALID_REQUEST: AuthorizationException = authEx(1000, "invalid_request")

    /** An `unauthorized_client` OAuth2 error response. */
    val UNAUTHORIZED_CLIENT: AuthorizationException = authEx(1001, "unauthorized_client")

    /** An `access_denied` OAuth2 error response. */
    val ACCESS_DENIED: AuthorizationException = authEx(1002, "access_denied")

    /** An `unsupported_response_type` OAuth2 error response. */
    val UNSUPPORTED_RESPONSE_TYPE: AuthorizationException =
      authEx(1003, "unsupported_response_type")

    /** An `invalid_scope` OAuth2 error response. */
    val INVALID_SCOPE: AuthorizationException = authEx(1004, "invalid_scope")

    /**
     * An `server_error` OAuth2 error response, equivalent to an HTTP 500 error code, but sent via
     * redirect.
     */
    val SERVER_ERROR: AuthorizationException = authEx(1005, "server_error")

    /**
     * A `temporarily_unavailable` OAuth2 error response, equivalent to an HTTP 503 error code, but
     * sent via redirect.
     */
    val TEMPORARILY_UNAVAILABLE: AuthorizationException = authEx(1006, "temporarily_unavailable")

    /**
     * An authorization error occurring on the client rather than the server. For example, due to
     * client misconfiguration. This error should be treated as unrecoverable.
     */
    val CLIENT_ERROR: AuthorizationException = authEx(1007, null)

    /**
     * Indicates an OAuth error as per RFC 6749, but the error code is not known to the AppAuth for
     * Android library. It could be a custom error or code, or one from an OAuth extension. The
     * [.error] field provides the exact error string returned by the server.
     */
    val OTHER: AuthorizationException = authEx(1008, null)

    /**
     * Indicates that the response state param did not match the request state param, resulting in
     * the response being discarded.
     */
    val STATE_MISMATCH: AuthorizationException =
      generalEx(9, "Response state param did not match request state")
    private val STRING_TO_EXCEPTION: Map<String, AuthorizationException> =
      exceptionMapByString(
        INVALID_REQUEST,
        UNAUTHORIZED_CLIENT,
        ACCESS_DENIED,
        UNSUPPORTED_RESPONSE_TYPE,
        INVALID_SCOPE,
        SERVER_ERROR,
        TEMPORARILY_UNAVAILABLE,
        CLIENT_ERROR,
        OTHER
      )

    fun exceptionMapByString(
      vararg exceptions: AuthorizationException?
    ): Map<String, AuthorizationException> {
      val map = mutableMapOf<String, AuthorizationException>()

      exceptions.forEach { ex -> ex?.error?.let { error -> map[error] = ex } }

      return map.toMap()
    }

    /**
     * Returns the matching exception type for the provided OAuth2 error string, or [.OTHER] if
     * unknown.
     */
    fun byString(error: String?): AuthorizationException {
      val ex = STRING_TO_EXCEPTION[error]
      return ex ?: OTHER
    }
  }
}
