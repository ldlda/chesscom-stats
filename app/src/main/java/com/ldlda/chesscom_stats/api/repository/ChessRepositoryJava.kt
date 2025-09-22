package com.ldlda.chesscom_stats.api.repository

import androidx.annotation.WorkerThread
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import com.ldlda.chesscom_stats.api.fetch.ChessApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.future // requires kotlinx-coroutines-jdk8
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

/**
 * Java-friendly adapter around ChessRepository.
 *
 * - get*Blocking: call from worker thread in tests or background code.
 * - get*Async: CompletableFuture for Java async tests/UI.
 */
class ChessRepositoryJava @JvmOverloads constructor(
    private val repo: ChessRepository = ChessRepositoryImpl()
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerBlocking(username: String): Player =
        runBlocking(Dispatchers.IO) { repo.getPlayer(username) }

    fun getPlayerAsync(username: String): CompletableFuture<Player> =
        scope.future { repo.getPlayer(username) }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerStatsBlocking(username: String): PlayerStats =
        runBlocking(Dispatchers.IO) { repo.getPlayerStats(username) }

    fun getPlayerStatsAsync(username: String): CompletableFuture<PlayerStats> =
        scope.future { repo.getPlayerStats(username) }

    /** Call in test teardown if needed to stop any in-flight work. */
    fun close() {
        scope.cancel()
    }
}