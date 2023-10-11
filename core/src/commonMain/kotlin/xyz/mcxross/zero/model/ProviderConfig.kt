package xyz.mcxross.zero.model

interface ProviderConfig {
  var clientId: String
  var redirectUri: String
  var responseType: String
  var scope: String
}

data class Google(
  override var clientId: String,
  override var redirectUri: String,
  override var responseType: String,
  override var scope: String
) : ProviderConfig

data class Facebook(
  override var clientId: String,
  override var redirectUri: String,
  override var responseType: String,
  override var scope: String
) : ProviderConfig

data class Twitch(
  override var clientId: String,
  override var redirectUri: String,
  override var responseType: String,
  override var scope: String
) : ProviderConfig
