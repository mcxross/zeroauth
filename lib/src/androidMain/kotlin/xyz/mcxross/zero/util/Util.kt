package xyz.mcxross.zero.util

import kotlinx.serialization.json.Json
import xyz.mcxross.zero.model.ZKLoginResponse

fun jsonToZKLoginResponse(json: String): ZKLoginResponse =
    Json.decodeFromString(ZKLoginResponse.serializer(), json)
