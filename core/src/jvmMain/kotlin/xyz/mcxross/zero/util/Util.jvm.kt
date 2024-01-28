package xyz.mcxross.zero.util

actual fun generateRandomness(): String = "randomness"

actual fun generateNonce(pubKey: Any, maxEpoch: Int, randomness: String): String = "nonce"

actual fun generateNonce(
    pubKey: String,
    schema: String,
    maxEpoch: Long,
    randomness: String
): String = "nonce"
