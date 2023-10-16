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
import xyz.mcxross.zero.oauth.AdditionalParamsProcessor.builtInParams

@Serializable
data class EndSessionRequest(
  var idTokenHint: String,
  var postLogoutRedirectUri: String,
  var uiLocales: String,
  var additionalParameters: Map<String, String>,
  override val state: String,
) : AuthorizationManagementRequest {
  val PARAM_ID_TOKEN_HINT = "id_token_hint"

  val PARAM_POST_LOGOUT_REDIRECT_URI = "post_logout_redirect_uri"

  val PARAM_STATE = "state"

  val PARAM_UI_LOCALES = "ui_locales"

  private val BUILT_IN_PARAMS: Set<String> =
    builtInParams(
      PARAM_ID_TOKEN_HINT,
      PARAM_POST_LOGOUT_REDIRECT_URI,
      PARAM_STATE,
      PARAM_UI_LOCALES
    )

  private val KEY_CONFIGURATION = "configuration"
  private val KEY_ID_TOKEN_HINT = "id_token_hint"
  private val KEY_POST_LOGOUT_REDIRECT_URI = "post_logout_redirect_uri"
  private val KEY_STATE = "state"
  private val KEY_UI_LOCALES = "ui_locales"
  private val KEY_ADDITIONAL_PARAMETERS = "additionalParameters"

  override fun toUri(): Uri {
    TODO("Not yet implemented")
  }
}
