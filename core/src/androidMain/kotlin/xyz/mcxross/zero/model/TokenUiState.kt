package xyz.mcxross.zero.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import xyz.mcxross.zero.internal.Logger
import xyz.mcxross.zero.repository.TokenRepository

@Serializable
data class TokenUiState(
    val tokenInfo: TokenInfo?,
)

class TokenViewModel : ViewModel() {
  private val _tokenUiState = MutableStateFlow(TokenUiState(tokenInfo = null))
  val tokenUiState: StateFlow<TokenUiState?> = _tokenUiState

  private var fetchJob: Job? = null

  private val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

  private fun setToken(tokenInfo: TokenInfo) {
    _tokenUiState.value = TokenUiState(tokenInfo = tokenInfo)
  }

  private fun updateToken(tokenInfo: TokenInfo?) {
    _tokenUiState.update { it.copy(tokenInfo = tokenInfo) }
  }

  fun fetchToken(code: String, authorizationRequest: AuthorizationRequest) {
    Logger.debug(
        "${this::class.simpleName} performing token fetch for request $authorizationRequest")
    fetchJob?.cancel()
    fetchJob =
        viewModelScope.launch {
          when (val token =
              TokenRepository(Dispatchers.IO)
                  .fetchToken(code = code, authorizationRequest = authorizationRequest)) {
            is Result.Success -> updateToken(token.data)
            is Result.Error -> updateToken(null)
          }
        }
  }
}
