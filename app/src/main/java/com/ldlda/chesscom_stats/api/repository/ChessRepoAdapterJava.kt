package com.ldlda.chesscom_stats.api.repository

import androidx.annotation.WorkerThread
import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.playerstats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.ChessSearchItem
import com.ldlda.chesscom_stats.api.fetch.ChessApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import java.net.URI
import java.util.concurrent.CompletableFuture

/**
 * Java-friendly adapter around ChessRepository.
 *
 * - get*Blocking: call from worker thread in tests or background code.
 * - get*Async: CompletableFuture for Java async tests/UI.
 *
 * - also includes some convenient functions
 */
class ChessRepoAdapterJava @JvmOverloads constructor(
    private val repo: ChessRepository = ChessRepositoryTimedCache.defaultInstance,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) {
    private fun <T> runBlockingLda(task: suspend CoroutineScope.() -> T) =
        runBlocking(scope.coroutineContext, task)

    private fun <T> runAsyncLda(task: suspend CoroutineScope.() -> T) =
        scope.future(block = task)

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerBlocking(username: String): Player =
        runBlockingLda { repo.getPlayer(username) }

    fun getPlayerAsync(username: String): CompletableFuture<Player> =
        runAsyncLda { repo.getPlayer(username) }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerStatsBlocking(username: String): PlayerStats =
        runBlockingLda { repo.getPlayerStats(username) }

    fun getPlayerStatsAsync(username: String): CompletableFuture<PlayerStats> =
        runAsyncLda { repo.getPlayerStats(username) }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getLeaderboardsBlocking(): Leaderboards =
        runBlockingLda { repo.getLeaderboards() }

    fun getLeaderboardsAsync(): CompletableFuture<Leaderboards> =
        runAsyncLda { repo.getLeaderboards() }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getCountryByUrlBlocking(url: URI): CountryInfo =
        runBlockingLda { repo.getCountryByUrl(url) }

    fun getCountryByUrlAsync(url: URI): CompletableFuture<CountryInfo> =
        runAsyncLda { repo.getCountryByUrl(url) }

    fun getUsernameSuggestionsAsync(prefix: String): CompletableFuture<List<ChessSearchItem>> =
        runAsyncLda { repo.searchPlayers(prefix) }

    /* convenience functions */
    @WorkerThread
    @Throws(ChessApiException::class)
    fun runPlayerFetchCountryBlocking(player: Player): CountryInfo =
        runBlockingLda { player.fetchCountryInfo(repo) }

    fun runPlayerFetchCountryAsync(player: Player): CompletableFuture<CountryInfo> =
        runAsyncLda { player.fetchCountryInfo(repo) }

    fun playerUpdateCountryAsync(player: Player): CompletableFuture<Player> =
        runAsyncLda { player.fetchCountryInfo(repo); player }

    fun playerUpdateStatsAsync(player: Player): CompletableFuture<Player> =
        runAsyncLda { player.fetchPlayerStats(repo); player }

    // insanely convenient
    fun getCompletePlayerAsync(username: String): CompletableFuture<Player> =
        runAsyncLda {
            val player = repo.getPlayer(username)
            awaitAll(
                async { player.fetchCountryInfo(repo) },
                async { player.fetchPlayerStats(repo) }
            )
            player
        }

    /** Call in test teardown if needed to stop any in-flight work. */
    fun close() = scope.cancel()
}


