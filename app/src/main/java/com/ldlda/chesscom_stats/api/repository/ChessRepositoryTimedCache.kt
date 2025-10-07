package com.ldlda.chesscom_stats.api.repository

import androidx.annotation.VisibleForTesting
import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.playerstats.PlayerStats
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.utils.cache.TimedCache
import com.ldlda.chesscom_stats.utils.cache.TimedCacheProvider
import java.net.URI

class ChessRepositoryTimedCache(
    client: ChessApiClient = ChessApiClient.defaultInstance,
    private val playerCache: TimedCache<Player> = TimedCacheProvider.defaultPlayerCache,
    private val statsCache: TimedCache<PlayerStats> = TimedCacheProvider.defaultStatsCache,
    private val leaderboardsCache: TimedCache<Leaderboards> = TimedCacheProvider.defaultLeaderboardsCache,
    private val countryCache: TimedCache<CountryInfo> = TimedCacheProvider.defaultCountryCache,
) : ChessRepositoryImpl(client) {
    companion object {
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


    override suspend fun getCountryByUrl(countryUrl: URI): CountryInfo {
        val url = countryUrl.toString()
        countryCache.get(url)?.let { return it }
        val info = super.getCountryByUrl(countryUrl)
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

