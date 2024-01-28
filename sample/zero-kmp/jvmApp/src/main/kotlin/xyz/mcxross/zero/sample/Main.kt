package xyz.mcxross.zero.sample

import xyz.mcxross.zero.feedback.callback.ZKLoginFeedback
import xyz.mcxross.zero.model.Google
import xyz.mcxross.zero.model.Nonce
import xyz.mcxross.zero.model.OpenIDServiceConfiguration
import xyz.mcxross.zero.model.Option
import xyz.mcxross.zero.model.ZKLoginRequest
import xyz.mcxross.zero.zkLogin

fun main() {

  val req =
    ZKLoginRequest(
      openIDServiceConfiguration =
        OpenIDServiceConfiguration(
          Google(),
          "<YOUR-CLIENT-ID>",
          "redirect",
          Nonce.FromPubKey("pubkey"),
        )
    )

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
}
