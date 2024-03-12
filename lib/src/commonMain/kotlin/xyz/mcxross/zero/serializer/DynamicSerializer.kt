package xyz.mcxross.zero.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DynamicSerializer : KSerializer<Any> {

  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("dynamic", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: Any) {
    encoder.encodeString(value.toString())
  }

  override fun deserialize(decoder: Decoder): Any {
    return decoder.decodeString()
  }
}
