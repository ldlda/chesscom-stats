package com.ldlda.chesscom_stats.api.repository

import androidx.annotation.WorkerThread
import com.ldlda.chesscom_stats.api.data.club.Club
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import com.ldlda.chesscom_stats.api.fetch.ChessApiException
import com.ldlda.chesscom_stats.util.Futures.eager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import java.util.concurrent.CompletableFuture

/**
 * Java-friendly adapter around ChessRepository.
 *
 * @deprecated Use {@link JavaChessRepository} instead. That interface is designed for Java
 * callers and returns CompletableFuture directly without exposing coroutines. The default
 * implementation {@link ChessRepositoryTimedCache} already implements both interfaces.
 * <p>
 * Migration example:
 * <pre>{@code
 * // Old (this class):
 * ChessRepoAdapterJava adapter = new ChessRepoAdapterJava();
 * adapter.getPlayerAsync("hikaru").thenAccept(...);
 *
 * // New (JavaChessRepository):
 * JavaChessRepository repo = ChessRepositoryTimedCache.getDefaultInstance();
 * repo.getPlayer("hikaru").thenAccept(...);
 * }</pre>
 * <p>
 * This class will be removed in the future.
 *
 * - get*Blocking: call from worker thread in tests or background code.
 * - get*Async: CompletableFuture for Java async tests/UI.
 *
 * - also includes some convenient functions
 */

class ChessRepoAdapterJava<CR>(
    val repo: CR,
    val scope: CoroutineScope
) : JavaChessRepository where CR : ChessRepository {
    private fun <T> runBlockingLda(task: suspend CoroutineScope.() -> T) =
        runBlocking(scope.coroutineContext, task)

    private fun <T> runAsyncLda2(task: suspend CoroutineScope.() -> T) =
        eager(scope.coroutineContext, task)

    private fun <T> runAsyncLda(task: suspend CoroutineScope.() -> T) =
        scope.future(block = task)

    fun close() = scope.cancel()

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerBlocking(username: String): Player =
        runBlockingLda { repo.getPlayer(username) }

    override fun getPlayerAsync(username: String): CompletableFuture<Player> =
        runAsyncLda { repo.getPlayer(username) }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerStatsBlocking(username: String): PlayerStats =
        runBlockingLda { repo.getPlayerStats(username) }

    override fun getPlayerStatsAsync(username: String): CompletableFuture<PlayerStats> =
        runAsyncLda { repo.getPlayerStats(username) }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getLeaderboardsBlocking(): Leaderboards =
        runBlockingLda { repo.getLeaderboards() }

    override fun getLeaderboardsAsync(): CompletableFuture<Leaderboards> =
        runAsyncLda { repo.getLeaderboards() }

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getCountryByUrlBlocking(url: HttpUrl): CountryInfo =
        runBlockingLda { repo.getCountry(url) }

    override fun getCountryByUrlAsync(url: HttpUrl): CompletableFuture<CountryInfo> =
        runAsyncLda { repo.getCountry(url) }


    override fun searchPlayersAsync(prefix: String): CompletableFuture<List<SearchItem>> =
        runAsyncLda { repo.searchPlayers(prefix) }


    override fun getClubAsync(nameId: String): CompletableFuture<Club> =
        runAsyncLda { repo.getClub(nameId) }

    override fun getClubAsync(clubUrl: HttpUrl): CompletableFuture<Club> =
        runAsyncLda { repo.getClub(clubUrl) }


    override fun getCountryClubsAsync(code: String): CompletableFuture<List<HttpUrl>> =
        runAsyncLda { repo.getCountryClubs(code) }

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

    // ridiculously convenient
    companion object {
        @JvmStatic
        @JvmSuppressWildcards
        fun <T : ChessRepository> T.getAdapterJava(scope: CoroutineScope) =
            ChessRepoAdapterJava(this, scope)
    }
}


