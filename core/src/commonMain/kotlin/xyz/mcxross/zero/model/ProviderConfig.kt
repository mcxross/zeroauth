package xyz.mcxross.zero.model

interface ProviderConfig {
  var clientId: String
  var redirectUri: String
  var nonce: String
}

data class Google(
  override var clientId: String,
  override var redirectUri: String,
  override var nonce: String
) : ProviderConfig

data class Facebook(
  override var clientId: String,
  override var redirectUri: String,
  override var nonce: String
) : ProviderConfig

data class Twitch(
  override var clientId: String,
  override var redirectUri: String,
  override var nonce: String
) : ProviderConfig
