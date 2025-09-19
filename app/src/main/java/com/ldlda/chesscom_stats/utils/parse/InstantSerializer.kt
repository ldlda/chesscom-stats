package com.ldlda.chesscom_stats.utils.parse

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.DateTimeException
import java.time.Instant

/** Epoch Second */
object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) {
        val s = value.epochSecond
        encoder.encodeLong(s)
    }

    override fun deserialize(decoder: Decoder): Instant {
        val ms = try {
            decoder.decodeLong()
        } catch (e: SerializationException) {
            throw SerializationException(
                "Expected Long, got something else",
                e
            )
        }
        return try {
            Instant.ofEpochSecond(ms)
        } catch (e: DateTimeException) {
            throw SerializationException("Invalid epoch millis: $ms", e)
        }
    }

}