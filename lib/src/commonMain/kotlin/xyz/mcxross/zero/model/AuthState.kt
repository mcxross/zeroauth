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

data class AuthState(
  val refreshToken: String,
  val scope: String,
  val lastAuthorizationResponse: String,
  val lastTokenResponse: String,
) {
  /**
   * Tokens which have less time than this value left before expiry will be considered to be expired
   * for the purposes of calls to [ performActionWithFreshTokens][.performActionWithFreshTokens].
   */
  val EXPIRY_TIME_TOLERANCE_MS = 60000

  private val KEY_CONFIG = "config"
  private val KEY_REFRESH_TOKEN = "refreshToken"
  private val KEY_SCOPE = "scope"
  private val KEY_LAST_AUTHORIZATION_RESPONSE = "lastAuthorizationResponse"
  private val KEY_LAST_TOKEN_RESPONSE = "mLastTokenResponse"
  private val KEY_AUTHORIZATION_EXCEPTION = "mAuthorizationException"
  private val KEY_LAST_REGISTRATION_RESPONSE = "lastRegistrationResponse"
}
