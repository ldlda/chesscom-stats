package com.ldlda.chesscom_stats.utils.serialize

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URI
import java.net.URISyntaxException

abstract class ToStringSerializer<T>(
    val name: String,
) : KSerializer<T> {
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


/// uri is a safer url. it also doesn't do anything to verify the url (namely doing some dns resolves)
object URISerializer : ToStringSerializer<URI>("URI") {
    override fun fromString(string: String): URI {
        return try {
            URI(string)
        } catch (e: URISyntaxException) {
            throw SerializationException("Invalid URI string: '$string'", e)
        }
    }
}
