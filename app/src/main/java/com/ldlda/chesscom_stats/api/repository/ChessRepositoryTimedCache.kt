package com.ldlda.chesscom_stats.api.repository

import androidx.annotation.VisibleForTesting
import com.ldlda.chesscom_stats.api.data.country.CountryClubs
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.country.CountryPlayers
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.util.cache.TimedCache
import com.ldlda.chesscom_stats.util.cache.TimedCacheProvider
import okhttp3.HttpUrl

class ChessRepositoryTimedCache(
    client: ChessApiClient = ChessApiClient.defaultInstance,
    private val playerCache: TimedCache<Player> = TimedCacheProvider.defaultPlayerCache,
    private val statsCache: TimedCache<PlayerStats> = TimedCacheProvider.defaultStatsCache,
    private val leaderboardsCache: TimedCache<Leaderboards> = TimedCacheProvider.defaultLeaderboardsCache,
    private val countryCache: TimedCache<CountryInfo> = TimedCacheProvider.defaultCountryCache,
    private val countryPlayerCache: TimedCache<CountryPlayers> = TimedCache(15 * 60_000),
    private val countryClubCache: TimedCache<CountryClubs> = TimedCache(15 * 60_000),

    ) : ChessRepositoryImpl(client) {
    companion object {
        /**
         * Default singleton instance with 2-minute TTL cache.
         *
         * Kotlin usage:
         * ```kotlin
         * val repo: ChessRepository = ChessRepositoryTimedCache.defaultInstance
         * val player = repo.getPlayer("hikaru") // suspend
         * ```
         *
         * Java usage (casts to JavaChessRepository):
         * ```java
         * JavaChessRepository repo = ChessRepositoryTimedCache.getDefaultInstance();
         * repo.getPlayerAsync("hikaru").thenAccept(player -> {
         *     // use player
         * });
         * ```
         */
        @JvmStatic
        val defaultInstance = ChessRepositoryTimedCache()
        private const val LEADERBOARDS_KEY = "leaderboards"
    }

    override suspend fun getPlayer(username: String): Player {
        val key = username.normalize()
        playerCache.get(key)?.let { return it }
        val player = super.getPlayer(key)
        playerCache.put(key, player)
        return player
    }

    override suspend fun getPlayerStats(username: String): PlayerStats {
        val key = username.normalize()
        statsCache.get(key)?.let { return it }
        val stats = super.getPlayerStats(key)
        statsCache.put(key, stats)
        return stats
    }

    override suspend fun getLeaderboards(): Leaderboards {
        leaderboardsCache.get(LEADERBOARDS_KEY)?.let { return it }
        val boards = super.getLeaderboards()
        leaderboardsCache.put(LEADERBOARDS_KEY, boards)
        return boards
    }


    override suspend fun getCountry(countryUrl: HttpUrl): CountryInfo {
        val url = countryUrl.toString()
        countryCache.get(url)?.let { return it }
        val info = super.getCountry(countryUrl)
        countryCache.put(url, info)
        return info
    }

    private fun String.normalize(): String = trim().lowercase()

    @VisibleForTesting
    fun clearCaches() {
        playerCache.clear()
        statsCache.clear()
        leaderboardsCache.clear()
    }
}

