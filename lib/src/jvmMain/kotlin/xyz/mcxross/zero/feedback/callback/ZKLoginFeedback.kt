package xyz.mcxross.zero.feedback.callback

import xyz.mcxross.zero.model.Option

interface ZKLoginFeedback {
  fun onToken(option: Option<String>)

  fun onProof(option: Option<String>)

  fun onSalt(option: Option<String>)
}
