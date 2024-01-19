package xyz.mcxross.zero.util

expect fun generateRandomness(): String

expect fun generateNonce(pubKey: Any, maxEpoch: Int, randomness: String): String

expect fun generateNonce(pubKey: String, schema: String, maxEpoch: Long, randomness: String): String
