package xyz.mcxross.zero.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import xyz.mcxross.zero.model.KeyDetails

object KeyDetailsSerializer : KSerializer<KeyDetails> {
  override val descriptor: SerialDescriptor =
      buildClassSerialDescriptor("KeyDetails") {
        element<String>("address")
        element<String>("sk")
        element<String>("phrase")
      }

  override fun serialize(encoder: Encoder, value: KeyDetails) {
    val jsonElement =
        JsonObject(
            mapOf(
                "address" to JsonPrimitive(value.address),
                "sk" to JsonPrimitive(value.sk),
                "phrase" to JsonPrimitive(value.phrase)))
    encoder.encodeString(jsonElement.toString())
  }

  override fun deserialize(decoder: Decoder): KeyDetails {
    val json = decoder.decodeString()
    val jsonObject = Json.parseToJsonElement(json).jsonObject
    return KeyDetails(
        jsonObject.getValue("address").jsonPrimitive.content,
        jsonObject.getValue("sk").jsonPrimitive.content,
        jsonObject.getValue("phrase").jsonPrimitive.content)
  }
}
