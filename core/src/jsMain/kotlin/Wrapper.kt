@file:JsModule("@mysten/zklogin")
@file:JsNonModule
@JsName("generateRandomness") external fun generateRandomnessJs(): String

@JsName("generateNonce")
external fun generateNonceJs(pubKey: String, maxEpoch: Long, randomness: String): String
