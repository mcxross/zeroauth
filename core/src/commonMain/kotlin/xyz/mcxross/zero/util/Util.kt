package xyz.mcxross.zero.util

expect fun generateRandomness(): String

expect fun generateNonce(pubKey: String, maxEpoch: Long, randomness: String): String
