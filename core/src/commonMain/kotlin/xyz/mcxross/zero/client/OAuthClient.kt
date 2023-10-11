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

class OAuthClient : Client {
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
