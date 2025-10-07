package com.ldlda.chesscom_stats.utils.cache

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.playerstats.PlayerStats

object TimedCacheProvider {
    val defaultPlayerCache = TimedCache<Player>(ttlMillis = 5 * 60_000L)
    val defaultStatsCache = TimedCache<PlayerStats>(ttlMillis = 2 * 60_000L)
    val defaultLeaderboardsCache = TimedCache<Leaderboards>(ttlMillis = 15 * 60_000L)
    val defaultCountryCache = TimedCache<CountryInfo>(ttlMillis = 24 * 60 * 60_000L)
}