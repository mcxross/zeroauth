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
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.Serializable
import xyz.mcxross.zero.extension.toUri
import xyz.mcxross.zero.oauth.AdditionalParamsProcessor.builtInParams
import xyz.mcxross.zero.serializer.UriSerializer
import xyz.mcxross.zero.util.appendQueryParameterIfNotNull

/**
 * All spec-defined values for the OpenID Connect 1.0 `display` parameter.
 *
 * @see "OpenID Connect Core 1.0, Section 3.1.2.1
 *
 * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
sealed class Display(val value: String) {
  /**
   * The Authorization Server _SHOULD_ display the authentication and consent UI consistent with a
   * full User Agent page view. If the display parameter is not specified, this is the default
   * display mode.
   */
  @Serializable data object Page : Display("page")

  /**
   * The Authorization Server _SHOULD_ display the authentication and consent UI consistent with a
   * popup User Agent window. The popup User Agent window should be of an appropriate size for a
   * login-focused dialog and should not obscure the entire window that it is popping up over.
   */
  @Serializable data object Popup : Display("popup")

  /**
   * The Authorization Server _SHOULD_ display the authentication and consent UI consistent with a
   * device that leverages a touch interface.
   */
  @Serializable data object Touch : Display("touch")

  /**
   * The Authorization Server _SHOULD_ display the authentication and consent UI consistent with a
   * "feature phone" type display.
   */
  @Serializable data object Wap : Display("wap")
}

/**
 * All spec-defined values for the OpenID Connect 1.0 `prompt` parameter.
 *
 * @see "OpenID Connect Core 1.0, Section 3.1.2.1
 *
 * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
sealed class Prompt(val value: String) {
  /**
   * The Authorization Server _MUST NOT_ display any authentication or consent user interface pages.
   * An error is returned if an End-User is not already authenticated or the Client does not have
   * pre-configured consent for the requested Claims or does not fulfill other conditions for
   * processing the request. The error code will typically be `login_required`,
   * `interaction_required`, or another code defined in [OpenID Connect Core 1.0, Section 3.1.2.6](
   * https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.6). This can be used as
   * a method to check for existing authentication and/or consent.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.6
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.6>"
   */
  @Serializable data object None : Prompt("none")

  /**
   * The Authorization Server _SHOULD_ prompt the End-User for re-authentication. If it cannot
   * re-authenticate the End-User, it _MUST_ return an error, typically `login_required`.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.6
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.6>"
   */
  @Serializable data object Login : Prompt("login")

  /**
   * The Authorization Server _SHOULD_ prompt the End-User for consent before returning information
   * to the Client. If it cannot obtain consent, it _MUST_ return an error, typically
   * `consent_required`.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.6
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.6>"
   */
  @Serializable data object Consent : Prompt("consent")

  /**
   * The Authorization Server _SHOULD_ prompt the End-User to select a user account. This enables an
   * End-User who has multiple accounts at the Authorization Server to select amongst the multiple
   * accounts that they might have current sessions for. If it cannot obtain an account selection
   * choice made by the End-User, it MUST return an error, typically `account_selection_required`.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.6
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.6>"
   */
  @Serializable data object SelectAccount : Prompt("select_account")
}

/**
 * All spec-defined values for the OAuth2 / OpenID Connect 1.0 `scope` parameter.
 *
 * @see "OpenID Connect Core 1.0, Section 5.4
 *
 * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.5.4>"
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
sealed class Scope(val value: String) {
  /**
   * A scope for the authenticated user's mailing address.
   *
   * @see "OpenID Connect Core 1.0, Section 5.4
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.5.4>"
   */
  @Serializable data object Address : Scope("address")

  /**
   * A scope for the authenticated user's email address.
   *
   * @see "OpenID Connect Core 1.0, Section 5.4
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.5.4>"
   */
  @Serializable data object Email : Scope("email")

  /**
   * A scope for requesting an OAuth 2.0 refresh token to be issued, that can be used to obtain an
   * Access Token that grants access to the End-User's UserInfo Endpoint even when the End-User is
   * not present (not logged in).
   *
   * @see "OpenID Connect Core 1.0, Section 11
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.11>"
   */
  @Serializable data object OfflineAccess : Scope("offline_access")

  /**
   * A scope for OpenID based authorization.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   */
  @Serializable data object OpenID : Scope("openid")

  /**
   * A scope for the authenticated user's phone number.
   *
   * @see "OpenID Connect Core 1.0, Section 5.4
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.5.4>"
   */
  @Serializable data object Phone : Scope("phone")

  /**
   * A scope for the authenticated user's basic profile information.
   *
   * @see "OpenID Connect Core 1.0, Section 5.4
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.5.4>"
   */
  @Serializable data object Profile : Scope("profile")
}

/**
 * All spec-defined values for the OAuth2 / OpenID Connect `response_mode` parameter.
 *
 * @see "OAuth 2.0 Multiple Response Type Encoding Practices, Section 2.1
 *
 * <http://openid.net/specs/oauth-v2-multiple-response-types-1_0.html#rfc.section.2.1>"
 *
 * @see "OpenID Connect Core 1.0, Section 3.1.2.1
 *
 * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
sealed class ResponseMode(val value: String) {
  /**
   * Instructs the authorization server to send response parameters using the query portion of the
   * redirect URI.
   *
   * @see "OAuth 2.0 Multiple Response Type Encoding Practices, Section 2.1
   *
   * <http://openid.net/specs/oauth-v2-multiple-response-types-1_0.html#rfc.section.2.1>"
   */
  @Serializable data object Query : ResponseMode("query")

  /**
   * Instructs the authorization server to send response parameters using the fragment portion of
   * the redirect URI.
   *
   * @see "OAuth 2.0 Multiple Response Type Encoding Practices, Section 2.1
   *
   * <http://openid.net/specs/oauth-v2-multiple-response-types-1_0.html#rfc.section.2.1>"
   */
  @Serializable data object Fragment : ResponseMode("fragment")
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class AuthorizationRequest(
  /**
   * The service's {@link AuthorizationServiceConfiguration configuration}. This configuration
   * specifies how to connect to a particular OAuth provider. Configurations may be {@link
   * AuthorizationServiceConfiguration#AuthorizationServiceConfiguration(Uri, Uri, Uri, Uri)}
   * created manually}, or {@link AuthorizationServiceConfiguration#fetchFromUrl(Uri,
   * AuthorizationServiceConfiguration.RetrieveConfigurationCallback)} via an OpenID Connect
   * Discovery Document}.
   */
  val configuration: AuthorizationServiceConfiguration,

  /**
   * The client identifier.
   *
   * @see "The OAuth 2.0 Authorization Framework (RFC 6749), Section 4
   *
   * <https://tools.ietf.org/html/rfc6749#section-4>"
   *
   * @see "The OAuth 2.0 Authorization Framework (RFC 6749), Section 4.1.1
   *
   * <https://tools.ietf.org/html/rfc6749#section-4.1.1>"
   */
  var clientId: String,

  /**
   * The OpenID Connect 1.0 `display` parameter. This is a string that specifies how the
   * Authorization Server displays the authentication and consent user interface pages to the
   * End-User.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   */
  var display: Display? = null,

  /**
   * The OpenID Connect 1.0 `login_hint` parameter. This is a string hint to the Authorization
   * Server about the login identifier the End-User might use to log in, typically collected
   * directly from the user in an identifier-first authentication flow.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   */
  var loginHint: String? = null,

  /**
   * The OpenID Connect 1.0 `prompt` parameter. This is a space delimited, case sensitive list of
   * ASCII strings that specifies whether the Authorization Server prompts the End-User for
   * re-authentication and consent.
   *
   * @see Prompt
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   */
  var prompt: Prompt? = null,

  /**
   * The OpenID Connect 1.0 `ui_locales` parameter. This is a space-separated list of BCP47
   * [RFC5646] language tag values, ordered by preference. It represents End-User's preferred
   * languages and scripts for the user interface.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   */
  var uiLocales: String? = null,

  /**
   * The expected response type.
   *
   * @see "The OAuth 2.0 Authorization Framework (RFC 6749), Section 3.1.1
   *
   * <https://tools.ietf.org/html/rfc6749#section-3.1.1>"
   *
   * @see "OpenID Connect Core 1.0, Section 3
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3>"
   */
  var responseType: String,

  /**
   * The client's redirect URI.
   *
   * @see "The OAuth 2.0 Authorization Framework (RFC 6749), Section 3.1.2
   *
   * <https://tools.ietf.org/html/rfc6749#section-3.1.2>"
   */
   var redirectUri: String,

  /**
   * The optional set of scopes expressed as a space-delimited, case-sensitive string.
   *
   * @see "The OAuth 2.0 Authorization Framework (RFC 6749), Section 3.1.2
   *
   * <https://tools.ietf.org/html/rfc6749#section-3.1.2>"
   *
   * @see "The OAuth 2.0 Authorization Framework (RFC 6749), Section 3.3
   *
   * <https://tools.ietf.org/html/rfc6749#section-3.3>"
   */
  var scope: Scope? = null,

  /**
   * An opaque value used by the client to maintain state between the request and callback. If this
   * value is not explicitly set, this library will automatically add state and perform appropriate
   * validation of the state in the authorization response. It is recommended that the default
   * implementation of this parameter be used wherever possible. Typically used to prevent CSRF
   * attacks, as recommended in
   * [RFC6819 Section 5.3.5](https://tools.ietf.org/html/rfc6819#section-5.3.5).
   *
   * @see "The OAuth 2.0 Authorization Framework (RFC 6749), Section 4.1.1
   *
   * <https://tools.ietf.org/html/rfc6749#section-4.1.1>"
   *
   * @see "The OAuth 2.0 Authorization Framework (RFC 6749), Section 5.3.5
   *
   * <https://tools.ietf.org/html/rfc6749#section-5.3.5>"
   */
  override val state: String? = null,

  /**
   * String value used to associate a Client session with an ID Token, and to mitigate replay
   * attacks. The value is passed through unmodified from the Authentication Request to the ID
   * Token. If this value is not explicitly set, this library will automatically add nonce and
   * perform appropriate validation of the ID Token. It is recommended that the default
   * implementation of this parameter be used wherever possible.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   */
  var nonce: String,

  /**
   * The proof key for code exchange. This is an opaque value used to associate an authorization
   * request with a subsequent code exchange, in order to prevent any eavesdropping party from
   * intercepting and using the code before the original requestor. If PKCE is disabled due to a
   * non-compliant authorization server which rejects requests with PKCE parameters present, this
   * value will be `null`.
   *
   * @see "Proof Key for Code Exchange by OAuth Public Clients (RFC 7636)
   *
   * <https://tools.ietf.org/html/rfc7636>"
   */
  var codeVerifier: String? = null,

  /**
   * The challenge derived from the {@link #codeVerifier code verifier}, using the
   * {@link #codeVerifierChallengeMethod challenge method}. If a code verifier is not being used for
   * this request, this value will be `null`.
   *
   * @see "Proof Key for Code Exchange by OAuth Public Clients (RFC 7636)
   *
   * <https://tools.ietf.org/html/rfc7636>"
   */
  var codeVerifierChallenge: String? = null,

  /**
   * The challenge method used to generate a {@link #codeVerifierChallenge challenge} from the
   * {@link #codeVerifier code verifier}. If a code verifier is not being used for this request,
   * this value will be `null`.
   *
   * @see "Proof Key for Code Exchange by OAuth Public Clients (RFC 7636)
   *
   * <https://tools.ietf.org/html/rfc7636>"
   */
  var codeVerifierChallengeMethod: String? = null,

  /**
   * Instructs the authorization service on the mechanism to be used for returning response
   * parameters from the authorization endpoint. This use of this parameter is _not recommended_
   * when the response mode that would be requested is the default mode specified for the response
   * type.
   *
   * @see "OpenID Connect Core 1.0, Section 3.1.2.1
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.2.1>"
   */
  var responseMode: ResponseMode? = null,

  /**
   * Requests that specific Claims be returned. The value is a Map listing the requested Claims.
   *
   * @see "OpenID Connect Core 1.0, Section 5.5
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.5.5>"
   */
  var claims: Map<String, String>? = null,

  /**
   * End-User's preferred languages and scripts for Claims being returned, represented as a
   * space-separated list of BCP47 [RFC5646] language tag values, ordered by preference.
   *
   * @see "OpenID Connect Core 1.0, Section 5.2
   *
   * <https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.5.2>"
   */
  var claimsLocales: String? = null,

  /**
   * Additional parameters to be passed as part of the request.
   *
   * @see "The OAuth 2.0 Authorization Framework (RFC 6749), Section 3.1
   *
   * <https://tools.ietf.org/html/rfc6749#section-3.1>"
   */
  var additionalParameters: Map<String, String>? = null
) : AuthorizationManagementRequest() {

  companion object {

    const val PARAM_CLIENT_ID = "client_id"

    const val PARAM_CODE_CHALLENGE = "code_challenge"

    const val PARAM_CODE_CHALLENGE_METHOD = "code_challenge_method"

    const val PARAM_DISPLAY = "display"

    const val PARAM_LOGIN_HINT = "login_hint"

    const val PARAM_PROMPT = "prompt"

    const val PARAM_UI_LOCALES = "ui_locales"

    const val PARAM_REDIRECT_URI = "redirect_uri"

    const val PARAM_RESPONSE_MODE = "response_mode"

    const val PARAM_RESPONSE_TYPE = "response_type"

    const val PARAM_SCOPE = "scope"

    const val PARAM_STATE = "state"

    const val PARAM_NONCE = "nonce"

    const val PARAM_CLAIMS = "claims"

    const val PARAM_CLAIMS_LOCALES = "claims_locales"

    private val BUILT_IN_PARAMS: Set<String> =
      builtInParams(
        PARAM_CLIENT_ID,
        PARAM_CODE_CHALLENGE,
        PARAM_CODE_CHALLENGE_METHOD,
        PARAM_DISPLAY,
        PARAM_LOGIN_HINT,
        PARAM_PROMPT,
        PARAM_UI_LOCALES,
        PARAM_REDIRECT_URI,
        PARAM_RESPONSE_MODE,
        PARAM_RESPONSE_TYPE,
        PARAM_SCOPE,
        PARAM_STATE,
        PARAM_CLAIMS,
        PARAM_CLAIMS_LOCALES,
      )
  }
}

fun AuthorizationRequest.toUri(): Uri {
  val uriBuilder =
    configuration.authorizationEndpoint
      .toUri()
      .buildUpon()
      .appendQueryParameter(AuthorizationRequest.PARAM_CLIENT_ID, clientId)
      .appendQueryParameter(AuthorizationRequest.PARAM_REDIRECT_URI, redirectUri.toString())
      .appendQueryParameter(AuthorizationRequest.PARAM_RESPONSE_TYPE, responseType)
      .appendQueryParameterIfNotNull(AuthorizationRequest.PARAM_DISPLAY, display?.value)
      .appendQueryParameterIfNotNull(AuthorizationRequest.PARAM_LOGIN_HINT, loginHint)
      .appendQueryParameterIfNotNull(AuthorizationRequest.PARAM_PROMPT, prompt?.value)
      .appendQueryParameterIfNotNull(AuthorizationRequest.PARAM_UI_LOCALES, uiLocales)
      .appendQueryParameterIfNotNull(AuthorizationRequest.PARAM_SCOPE, scope?.value)
      .appendQueryParameterIfNotNull(AuthorizationRequest.PARAM_STATE, state)
      .appendQueryParameterIfNotNull(AuthorizationRequest.PARAM_NONCE, nonce)

  if (codeVerifier != null) {
    uriBuilder.appendQueryParameter(
      AuthorizationRequest.PARAM_CODE_CHALLENGE,
      codeVerifierChallenge
    )
    uriBuilder.appendQueryParameter(
      AuthorizationRequest.PARAM_CODE_CHALLENGE_METHOD,
      codeVerifierChallengeMethod
    )
  }

  uriBuilder.appendQueryParameterIfNotNull(
    AuthorizationRequest.PARAM_RESPONSE_MODE,
    responseMode?.value
  )

  for ((key, value) in additionalParameters ?: emptyMap()) {
    uriBuilder.appendQueryParameter(key, value)
  }
  return uriBuilder.build()
}
