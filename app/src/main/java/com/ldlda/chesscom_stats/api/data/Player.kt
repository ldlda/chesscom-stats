package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.utils.parse.InstantSerializer
import com.ldlda.chesscom_stats.utils.parse.URISerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.time.Instant


/**
should be https://api.chess.com/pub/player/{ldabsbplef}
 */
@Serializable
data class Player(
    @SerialName("player_id")
    val playerId: Long,

    val username: String,

    @SerialName("url")
    @Serializable(with = URISerializer::class)
    val profilePage: URI,

    @SerialName("country")
    @Serializable(with = URISerializer::class)
    val countryCodeResource: URI,

    @SerialName("joined")
    @Serializable(with = InstantSerializer::class)
    val joined: Instant,

    @SerialName("last_online")
    @Serializable(with = InstantSerializer::class)
    val lastOnline: Instant,

    val followers: Int,

    @SerialName("avatar")
    @Serializable(with = URISerializer::class)
    val profilePictureResource: URI? = null,

    val title: TitleEnum? = null,

    /** name as in government name*/
    val name: String? = null,
) {
    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true }

        @JvmStatic
        fun fromJSON(jsonString: String): Player =
            jsonFormat.decodeFromString(serializer(), jsonString)
    }
}

/** Grandmaster of chess */
enum class TitleEnum {
    GM, WGM, IM, WIM, FM, WFM, NM, WNM, CM, WCM
}


data class Country(
    val name: String,
    val code: String // Locale.IsoCountryCode + random shit
)
