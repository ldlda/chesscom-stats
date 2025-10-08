package com.ldlda.chesscom_stats.api.data.search

import com.ldlda.chesscom_stats.util.serialize.tostring.InstantParseSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.URISerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URI
import java.time.Instant

@Serializable
data class ChessComUserView(
    val userId: Long,
    val username: String,

    @Serializable(ThisURISerializer::class)
    val avatar: URI,

    /**
    finally a use for [this fuckass][com.ldlda.chesscom_stats.api.fetch.ChessApiService.country]
     */
    val country: String,
    @Serializable(InstantParseSerializer::class)
    val createdAt: Instant,

    val firstName: String? = null,
    val lastName: String? = null,

    val chessTitle: String? = null,

    // one second after creation but i think im not certain
    // last profile update, above line is what if bro just joined
    @Serializable(InstantParseSerializer::class)
    val updatedAt: Instant? = null,

    ) {
    @Serializable
    class URISurrogate(
        @Serializable(URISerializer::class)
        @SerialName("highResolutionUrl")
        val url: URI,
    )


    class ThisURISerializer : KSerializer<URI> {
        override val descriptor = URISurrogate.serializer().descriptor
        override fun deserialize(decoder: Decoder): URI {
            val surrogate = decoder.decodeSerializableValue(URISurrogate.serializer())
            return surrogate.url
        }

        override fun serialize(
            encoder: Encoder,
            value: URI
        ) {
            // random ahh:
            encoder.encodeSerializableValue(URISurrogate.serializer(), URISurrogate(value))
        }
    }
}