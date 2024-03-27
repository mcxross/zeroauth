package xyz.mcxross.zero.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.mcxross.zero.client.DefaultClient
import xyz.mcxross.zero.model.AuthorizationRequest
import xyz.mcxross.zero.model.Result
import xyz.mcxross.zero.model.TokenInfo

class TokenRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) {
  suspend fun fetchToken(
      code: String,
      authorizationRequest: AuthorizationRequest
  ): Result<TokenInfo> {
    return withContext(dispatcher) {
      val client = DefaultClient("")

      val tokenInfo: TokenInfo =
          client.submitForm<TokenInfo>(
              authorizationRequest.configuration.tokenEndpoint,
              mapOf(
                  "grant_type" to "authorization_code",
                  "code" to code,
                  "redirect_uri" to authorizationRequest.redirectUri,
                  "client_id" to authorizationRequest.clientId))
      Result.Success(tokenInfo)
    }
  }
}
