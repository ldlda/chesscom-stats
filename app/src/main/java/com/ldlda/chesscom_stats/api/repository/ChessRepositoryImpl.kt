package com.ldlda.chesscom_stats.api.repository

import androidx.annotation.VisibleForTesting
import com.ldlda.chesscom_stats.api.data.Leaderboards
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.api.fetch.DefaultChessApi
import com.ldlda.chesscom_stats.utils.cache.TimedCache

class ChessRepositoryImpl(
    private val client: ChessApiClient = DefaultChessApi,
    private val playerCache: TimedCache<Player> = TimedCache(ttlMillis = 5 * 60_000L), // 5 min
    private val statsCache: TimedCache<PlayerStats> = TimedCache(ttlMillis = 2 * 60_000L), // 2 min
    private val leaderboardsCache: TimedCache<Leaderboards> = TimedCache(ttlMillis = 15 * 60_000L) // 15 min

) : ChessRepository {
    private companion object {
        private const val LEADERBOARDS_KEY = "leaderboards"
    }

    override suspend fun getPlayer(username: String): Player {
        val key = username.normalize()
        playerCache.get(key)?.let { return it }
        val player = client.execute { getPlayer(key) }
        playerCache.put(key, player)
        return player
    }

    override suspend fun getPlayerStats(username: String): PlayerStats {
        val key = username.normalize()
        statsCache.get(key)?.let { return it }
        val stats = client.execute { getPlayerStats(key) }
        statsCache.put(key, stats)
        return stats
    }

    override suspend fun getLeaderboards(): Leaderboards {
        leaderboardsCache.get(LEADERBOARDS_KEY)?.let { return it }
        val boards = client.execute { getLeaderboards() }
        leaderboardsCache.put(LEADERBOARDS_KEY, boards)
        return boards
    }

    private fun String.normalize(): String = trim().lowercase()

    @VisibleForTesting
    fun clearCaches() {
        playerCache.clear()
        statsCache.clear()
        leaderboardsCache.clear()
    }
}

