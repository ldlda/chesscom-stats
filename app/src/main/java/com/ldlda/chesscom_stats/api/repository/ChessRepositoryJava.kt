package com.ldlda.chesscom_stats.api.repository

import androidx.annotation.WorkerThread
import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.Leaderboards
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import com.ldlda.chesscom_stats.api.fetch.ChessApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
 */
class ChessRepositoryJava @JvmOverloads constructor(
    private val repo: ChessRepository = ChessRepositoryImpl(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) {

    private fun <T> runBlocking(task: suspend CoroutineScope.() -> T) =
        runBlocking(scope.coroutineContext, task)

    private fun <T> runAsync(task: suspend CoroutineScope.() -> T) =
        scope.future(block = task)

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerBlocking(username: String): Player =
        runBlocking { repo.getPlayer(username) }

    fun getPlayerAsync(username: String): CompletableFuture<Player> =
        runAsync { repo.getPlayer(username) }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerStatsBlocking(username: String): PlayerStats =
        runBlocking { repo.getPlayerStats(username) }

    fun getPlayerStatsAsync(username: String): CompletableFuture<PlayerStats> =
        runAsync { repo.getPlayerStats(username) }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getLeaderboardsBlocking(): Leaderboards =
        runBlocking { repo.getLeaderboards() }

    fun getLeaderboardsAsync(): CompletableFuture<Leaderboards> =
        runAsync { repo.getLeaderboards() }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getCountryByUrlBlocking(url: URI): CountryInfo =
        runBlocking { repo.getCountry(url) }

    fun getCountryByUrlAsync(url: URI): CompletableFuture<CountryInfo> =
        runAsync { repo.getCountry(url) }



    @WorkerThread
    @Throws(ChessApiException::class)
    fun runPlayerFetchCountryBlocking(player: Player): CountryInfo =
        runBlocking { player.fetchCountryInfo(repo) }

    /** convenience functions */
    fun runPlayerFetchCountryAsync(player: Player): CompletableFuture<CountryInfo> =
        runAsync { player.fetchCountryInfo(repo) }

    /** convenience function */
    fun playerUpdateCountryAsync(player: Player): CompletableFuture<Player> =
        runAsync { player.fetchCountryInfo(repo); player }


    /** Call in test teardown if needed to stop any in-flight work. */
    fun close() {
        scope.cancel()
    }

    companion object {
        val default = ChessRepositoryJava()
    }
}