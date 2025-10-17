package com.ldlda.chesscom_stats.di

import com.ldlda.chesscom_stats.api.data.club.Club
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyGame
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.puzzle.Puzzle
import com.ldlda.chesscom_stats.util.cache.TimedCache
import okhttp3.HttpUrl

object TimedCacheProvider {
    val defaultPlayerCache = TimedCache<Player>(ttlMillis = 5 * 60_000L)
    val defaultStatsCache = TimedCache<PlayerStats>(ttlMillis = 2 * 60_000L)
    val defaultLeaderboardsCache = TimedCache<Leaderboards>(ttlMillis = 15 * 60_000L)
    val defaultCountryCache = TimedCache<CountryInfo>(ttlMillis = 24 * 60 * 60_000L)

    val defaultCountryPlayerCache: TimedCache<List<String>> = TimedCache(5 * 60 * 60_000)

    val defaultCountryClubCache: TimedCache<List<HttpUrl>> = TimedCache(5 * 60 * 60_000)

    val defaultClubCache: TimedCache<Club> = TimedCache(10 * 60_000)

    val defaultMonthlyArchivesCache: TimedCache<List<HttpUrl>> = TimedCache(12 * 60 * 60_000)

    val defaultMonthlyGameListCache: TimedCache<List<MonthlyGame>> = TimedCache(15 * 60_000)

    val defaultPuzzleCache: TimedCache<Puzzle> = TimedCache(12 * 60 * 60_000)
}