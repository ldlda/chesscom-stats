@file:UseSerializers(InstantEpochSecondSerializer::class, HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.player

import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
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
    val profilePage: HttpUrl,

    @SerialName("country")
    val countryUrl: HttpUrl,

    // not the country tho Fuhh
    val location: String? = null,

    @SerialName("joined")
    val joined: Instant,

    @SerialName("last_online")
    val lastOnline: Instant,

    val status: Status,

    val followers: Int,

    @SerialName("avatar")
    val avatarUrl: HttpUrl? = null,

    val title: Title? = null,

    /** name as in government name */
    val name: String? = null,
) {
    var countryInfo: CountryInfo? = null
        internal set

    var playerStats: PlayerStats? = null
        internal set

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

    fun getCountryCode(base: String): String? =
        runCatching {
            CountryInfo.extractCountryCodeFromUrl(
                base,
                countryUrl.toString()
            )
        }.getOrNull()


    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true; prettyPrint = true }

        @JvmStatic
        fun fromJSON(jsonString: String): Player =
            jsonFormat.decodeFromString(jsonString)

        suspend fun fetchFullPlayer(repo: ChessRepository, username: String): Player =
        // the first time in my life it clicked:
        // this is a scope. Think a block. if the parent block cancels this cancel.
            // this goes with the parent. which mean call site do the work
            coroutineScope {
                val a = async {
                    repo.getPlayer(username)
                        .apply { fetchCountryInfo(repo) }
                }
                val b = async { repo.getPlayerStats(username) }
                val player = a.await()
                val stats = b.await()
                player.apply { playerStats = stats }
            }
    }

    fun toJSON() = jsonFormat.encodeToString(this)
}

