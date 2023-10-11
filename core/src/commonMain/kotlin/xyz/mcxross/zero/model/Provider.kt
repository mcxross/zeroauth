package xyz.mcxross.zero.model

enum class Provider {
  GOOGLE {
    override fun url(): String = "https://accounts.google.com/o/oauth2/v2/auth"
  },
  FACEBOOK {
    override fun url(): String = "https://www.facebook.com/v17.0/dialog/oauth"
  },
  TWITCH {
    override fun url(): String = "https://id.twitch.tv/oauth2/authorize"
  };

  abstract fun url(): String
}
