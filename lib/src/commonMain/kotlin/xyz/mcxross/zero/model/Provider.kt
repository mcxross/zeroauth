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

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Provider")
@Serializable
sealed class Provider {
  abstract val authorizationEndpoint: String
  abstract val tokenEndpoint: String
  abstract val revocationEndpoint: String?
  abstract val registrationEndpoint: String?
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Ghost")
@Serializable
data class Ghost(
  override val authorizationEndpoint: String = "",
  override val tokenEndpoint: String = "",
  override val revocationEndpoint: String? = null,
  override val registrationEndpoint: String? = null,
) : Provider()

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Facebook")
@Serializable
data class Facebook(
  override val authorizationEndpoint: String = "https://www.facebook.com/v2.8/dialog/oauth",
  override val tokenEndpoint: String = "https://graph.facebook.com/v2.8/oauth/access_token",
  override val revocationEndpoint: String? = null,
  override val registrationEndpoint: String? = null,
) : Provider()

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Google")
@Serializable
data class Google(
  override val authorizationEndpoint: String = "https://accounts.google.com/o/oauth2/v2/auth",
  override val tokenEndpoint: String = "https://www.googleapis.com/oauth2/v4/token",
  override val revocationEndpoint: String? = "https://accounts.google.com/o/oauth2/revoke",
  override val registrationEndpoint: String? = "https://accounts.google.com/o/oauth2/device/code",
) : Provider()

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Apple")
@Serializable
data class Apple(
  override val authorizationEndpoint: String = "https://appleid.apple.com/auth/authorize",
  override val tokenEndpoint: String = "https://appleid.apple.com/auth/token",
  override val revocationEndpoint: String? = null,
  override val registrationEndpoint: String? = null,
) : Provider()

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Twitch")
@Serializable
data class Twitch(
  override val authorizationEndpoint: String = "https://id.twitch.tv/oauth2/authorize",
  override val tokenEndpoint: String = "https://id.twitch.tv/oauth2/token",
  override val revocationEndpoint: String? = null,
  override val registrationEndpoint: String? = null,
) : Provider()

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Slack")
@Serializable
data class Slack(
  override val authorizationEndpoint: String = "https://slack.com/oauth/authorize",
  override val tokenEndpoint: String = "https://slack.com/api/oauth.access",
  override val revocationEndpoint: String? = null,
  override val registrationEndpoint: String? = null,
) : Provider()
