package com.ldlda.chesscom_stats.api.repository

import androidx.annotation.VisibleForTesting
import com.ldlda.chesscom_stats.api.data.club.Club
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyGame
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.puzzle.Puzzle
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.api.fetch.ChessApiService
import com.ldlda.chesscom_stats.api.fetch.PrivateApiService
import com.ldlda.chesscom_stats.di.TimedCacheProvider
import com.ldlda.chesscom_stats.util.cache.TimedCache
import okhttp3.HttpUrl
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ChessRepositoryTimedCache(
    publicService: ChessApiService = ChessApiClient.defaultPublicService,
    privateService: PrivateApiService = ChessApiClient.defaultPrivateService,
    private val playerCache: TimedCache<Player> = TimedCacheProvider.defaultPlayerCache,
    private val statsCache: TimedCache<PlayerStats> = TimedCacheProvider.defaultStatsCache,
    private val leaderboardsCache: TimedCache<Leaderboards> = TimedCacheProvider.defaultLeaderboardsCache,
    private val countryCache: TimedCache<CountryInfo> = TimedCacheProvider.defaultCountryCache,
//    private val countryPlayerCache: TimedCache<CountryPlayers> = TimedCacheProvider.defaultCountryPlayerCache,
    private val countryClubCache: TimedCache<List<HttpUrl>> = TimedCacheProvider.defaultCountryClubCache,
    private val clubsCache: TimedCache<Club> = TimedCacheProvider.defaultClubCache,
    private val monthlyArchivesCache: TimedCache<List<HttpUrl>> = TimedCacheProvider.defaultMonthlyArchivesCache,
    private val monthlyGameListCache: TimedCache<List<MonthlyGame>> = TimedCacheProvider.defaultMonthlyGameListCache,

    private val puzzleCache: TimedCache<Puzzle> = TimedCacheProvider.defaultPuzzleCache,

    ) : ChessRepositoryImpl(publicService, privateService) {
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
        private const val DAILY_PUZZLE_KEY = "dailyPuzzle"
        private const val RANDOM_PUZZLE_KEY = "randomPuzzle"

    }

    suspend fun <T> operation(
        store: TimedCache<T>,
        inputKeyBuilder: () -> String,
        responseKeyBuilder: ((String, T) -> String)? = null,
        og: suspend (String) -> T
    ): T {
        val key = inputKeyBuilder()
        store.get(key)?.let { return it }
        val info = og(key)
        val nKey = responseKeyBuilder?.let { it(key, info) } ?: key
        store.put(nKey, info)
        return info

    }


    override suspend fun getPlayer(username: String): Player {
        return operation(playerCache, { username.normalize() }) { super.getPlayer(it) }
//        val key = username.normalize()
//        playerCache.get(key)?.let { return it }
//        val player = super.getPlayer(key)
//        playerCache.put(key, player)
//        return player
    }

    override suspend fun getPlayerStats(username: String): PlayerStats {
        return operation(statsCache, { username.normalize() }) { super.getPlayerStats(it) }
//        val key = username.normalize()
//        statsCache.get(key)?.let { return it }
//        val stats = super.getPlayerStats(key)
//        statsCache.put(key, stats)
//        return stats
    }

    override suspend fun getLeaderboards(): Leaderboards {
        return operation(leaderboardsCache, { LEADERBOARDS_KEY }) { super.getLeaderboards() }
//        leaderboardsCache.get(LEADERBOARDS_KEY)?.let { return it }
//        val boards = super.getLeaderboards()
//        leaderboardsCache.put(LEADERBOARDS_KEY, boards)
//        return boards
    }


    override suspend fun getCountry(countryUrl: HttpUrl): CountryInfo {
        return operation(countryCache, HttpUrl::toString) { super.getCountry(countryUrl) }
//        val url = countryUrl.toString()
//        countryCache.get(url)?.let { return it }
//        val info = super.getCountry(countryUrl)
//        countryCache.put(url, info)
//        return info
    }

    override suspend fun getCountryClubs(code: String): List<HttpUrl> =
        operation(countryClubCache, { code.uppercase() }) {
            super.getCountryClubs(code)
        }

    override suspend fun getClub(url: HttpUrl): Club {
        return operation(clubsCache, HttpUrl::toString) { super.getClub(url) }
    }

    override suspend fun getMonthlyArchives(
        username: String,
        year: Int,
        month: Int
    ): List<MonthlyGame> = operation(
        monthlyGameListCache,
        // why repeat myself
        { "${username.normalize()}-${"%04d".format(year)}-${"%02d".format(month)}" }) {
        super.getMonthlyArchives(username, year, month)
    }

    override suspend fun getMonthlyArchivesList(username: String): List<HttpUrl> = operation(
        monthlyArchivesCache,
        { username.normalize() }) {
        super.getMonthlyArchivesList(username)
    }

    private val r =
        DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").withZone(ZoneId.of("Etc/GMT"))

    override suspend fun getDailyPuzzle(): Puzzle {
        return operation(puzzleCache, { DAILY_PUZZLE_KEY }, { _, daily ->
            r.format(daily.publishTime)
        }) { super.getDailyPuzzle() }
    }

    override suspend fun getRandomPuzzle(): Puzzle {
        return operation(puzzleCache, { RANDOM_PUZZLE_KEY }, { _, daily ->
            r.format(daily.publishTime)
        }) { super.getRandomPuzzle() }
    }

    private fun String.normalize(): String = trim().lowercase()

    @VisibleForTesting
    fun clearCaches() {
        playerCache.clear()
        statsCache.clear()
        leaderboardsCache.clear()
    }
}

