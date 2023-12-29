package xyz.mcxross.zero.util

actual fun generateRandomness(): String = ""

actual fun generateNonce(pubKey: Any, maxEpoch: Int, randomness: String): String = ""

actual fun generateNonce(
  pubKey: String,
  schema: String,
  maxEpoch: Long,
  randomness: String
): String = ""
