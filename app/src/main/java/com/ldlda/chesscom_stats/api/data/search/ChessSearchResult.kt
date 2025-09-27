package com.ldlda.chesscom_stats.api.data.search

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.utils.serialize.tostring.InstantParseSerializer
import com.ldlda.chesscom_stats.utils.serialize.tostring.URISerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.net.URI
import java.time.Instant

/**
 * These are what i see in a response.
 *
 * This can change any moment; as well as the [request][ChessSearchRequest]
 * and [endpoint][com.ldlda.chesscom_stats.api.fetch.ChessApiService.searchUsername].
 */
@Serializable
data class ChessSearchResult(
    val suggestions: List<ChessSearchItem> = emptyList()
) {
    companion object {
        private val jsonFormat =
            Json { ignoreUnknownKeys = true; prettyPrint = true; encodeDefaults = true }

        @JvmStatic
        fun fromJSON(jsonString: String): ChessSearchResult {
            println(jsonString)
            return jsonFormat.decodeFromString(jsonString)
        }
    }

    fun toJSON() = jsonFormat.encodeToString(this)
}

@Serializable
data class ChessSearchItem(
    val userView: ChessComUserView,
    val country: CountryInfo,
    @Serializable(with = InstantParseSerializer::class)
    val lastLoginDate: Instant,
    val bestRating: Int? = null,
)

@Serializable
data class ChessComUserView(
    val userId: Long,
    val username: String,

    @Serializable(ThisURISerializer::class)
    val avatar: URI, // NO

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
            throw NotImplementedError("we do not do this")
        }
    }
}


