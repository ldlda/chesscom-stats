@file:UseSerializers(InstantSerializer::class, URISerializer::class)

package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.utils.parse.InstantSerializer
import com.ldlda.chesscom_stats.utils.parse.URISerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
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
    val profilePage: URI,

    @SerialName("country")
    val countryUrl: URI,

    @SerialName("joined")
    val joined: Instant,

    @SerialName("last_online")
    val lastOnline: Instant,

    val status: String,

    val followers: Int,

    @SerialName("avatar")
    val avatarUrl: URI? = null,

    val title: String? = null,

    /** name as in government name*/
    val name: String? = null,
) {
    suspend fun fetchPlayerStats(repo: ChessRepository) =
        repo.getPlayerStats(username)
    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true }

        @JvmStatic
        fun fromJSON(jsonString: String): Player =
            jsonFormat.decodeFromString(serializer(), jsonString)
    }
}

@Serializable
data class Country(
    val name: String,
    val code: String // Locale.IsoCountryCode + random shit
)
