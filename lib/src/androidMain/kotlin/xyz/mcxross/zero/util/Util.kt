package xyz.mcxross.zero.util

import kotlinx.serialization.json.Json
import xyz.mcxross.sc.SuiCommons
import xyz.mcxross.sc.zklogin.ZKLogin
import xyz.mcxross.zero.model.ZKLoginResponse

actual fun generateRandomness(): String = SuiCommons.utils.generateRandomness()

actual fun generateNonce(pubKey: Any, maxEpoch: Int, randomness: String): String =
    ZKLogin.generateNonce(pubKey.toString(), maxEpoch.toULong(), randomness)

actual fun generateNonce(
    pubKey: String,
    schema: String,
    maxEpoch: Long,
    randomness: String
): String = ""

fun jsonToZKLoginResponse(json: String): ZKLoginResponse =
    Json.decodeFromString(ZKLoginResponse.serializer(), json)
