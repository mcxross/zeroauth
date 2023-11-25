package xyz.mcxross.zero.util

import generateNonceJs
import generateRandomnessJs

actual fun generateRandomness(): String = generateRandomnessJs()

actual fun generateNonce(pubKey: dynamic, maxEpoch: Int, randomness: String): String =
  generateNonceJs(pubKey, maxEpoch, randomness)

actual fun generateNonce(pubKey: String, schema: String, maxEpoch: Long, randomness: String): String =
  generateNonceJs(pubKey, schema, maxEpoch, randomness)
