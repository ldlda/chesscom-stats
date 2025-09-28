@file:UseSerializers(InstantEpochSecondSerializer::class, URISerializer::class)

package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.utils.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.utils.serialize.tostring.URISerializer
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

    /** name as in government name */
    val name: String? = null,
) {
    var countryInfo: CountryInfo? = null
        private set

    var playerStats: PlayerStats? = null
        private set

    suspend fun fetchPlayerStats(repo: ChessRepository): PlayerStats {
        val newPlayerStats = repo.getPlayerStats(username)
        playerStats?.takeIf { it == newPlayerStats }?.let { return it }
        playerStats = newPlayerStats
        return newPlayerStats
    }

    suspend fun fetchCountryInfo(repo: ChessRepository): CountryInfo {
        countryInfo?.let { return it }
        val countryInfo = repo.getCountry(countryUrl)
        this.countryInfo = countryInfo
        return countryInfo
    }

    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true; prettyPrint = true }

        @JvmStatic
        fun fromJSON(jsonString: String): Player =
            jsonFormat.decodeFromString(jsonString)
    }

    fun toJSON() = jsonFormat.encodeToString(this)
}

@Serializable
data class CountryInfo(
    val name: String,
    val code: String // Locale.IsoCountryCode + random shit
) {
    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true }

        @JvmStatic
        fun fromJSON(jsonString: String) =
            jsonFormat.decodeFromString(serializer(), jsonString)
    }

    fun toJSON() = jsonFormat.encodeToString(this)
}
