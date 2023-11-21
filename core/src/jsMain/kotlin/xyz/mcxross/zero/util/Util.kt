package xyz.mcxross.zero.util

import generateNonceJs
import generateRandomnessJs

actual fun generateRandomness(): String = generateRandomnessJs()

actual fun generateNonce(pubKey: String, maxEpoch: Long, randomness: String): String =
  generateNonceJs(pubKey, maxEpoch, randomness)
