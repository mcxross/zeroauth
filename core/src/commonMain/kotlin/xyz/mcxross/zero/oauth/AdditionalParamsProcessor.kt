package xyz.mcxross.zero.oauth

import kotlinx.serialization.json.Json
import xyz.mcxross.zero.model.AdditionalParams

object AdditionalParamsProcessor {
  fun builtInParams(vararg params: String): Set<String> {
    if (params.isEmpty()) {
      return emptySet()
    }
    return params.toSet()
  }

  fun extractAdditionalParams(jsonString: String): Map<String, String> {
    val additionalParams = Json.decodeFromString<AdditionalParams>(jsonString)

    return mapOf(
      "key1" to additionalParams.key1.orEmpty(),
      "key2" to additionalParams.key2.orEmpty()
    )
  }
}
