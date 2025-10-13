package com.ldlda.chesscom_stats.util.serialize.tostring

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

abstract class ToStringSerializer<T>(
    val name: String,
) : KSerializer<T> {
    @Throws(SerializationException::class)
    abstract fun fromString(string: String): T

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(name, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): T {
        val s = decoder.decodeString()
        return fromString(s)
    }
}


