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
package xyz.mcxross.zero.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import xyz.mcxross.zero.model.Provider
import xyz.mcxross.zero.model.ProviderConfig
import xyz.mcxross.zero.model.TokenInfo

class DefaultClient : Client {
  private val client = HttpClient(defaultEngine) { install(ContentNegotiation) { json() } }

  suspend fun request(
    provider: Provider,
    config: ProviderConfig,
  ) {
    val tokenInfo: TokenInfo =
      client
        .submitForm(
          url = provider.url(),
          formParameters =
            parameters {
              append("client_id", config.clientId)
              append("redirect_uri", config.redirectUri)
              append("scope", "openid")
              append("nonce", config.nonce)
              append("response_type", "id_token")
            }
        )
        .body()
  }
}
