zkLogin(
    req,
    object : ZKLoginFeedback {
      override fun onToken(option: Option<String>) {
        println("onToken: $option")
      }

      override fun onProof(option: Option<String>) {
        println("onProof: $option")
      }

      override fun onSalt(option: Option<String>) {
        println("onSalt: $option")
      }
    },
  )
