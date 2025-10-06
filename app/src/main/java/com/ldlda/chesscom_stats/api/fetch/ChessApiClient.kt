package com.ldlda.chesscom_stats.api.fetch

import androidx.annotation.WorkerThread
import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.Leaderboards
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.ChessSearchItem
import com.ldlda.chesscom_stats.api.data.search.ChessSearchRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.CompletableFuture

/*
    what is this bullshit
 */
class ChessApiClient : ChessApiBackend {
    companion object {
        const val CHESS_API_URL = "https://api.chess.com/"
        val defaultOkHttp = OkHttpClient.Builder()
            //  TODO: This are not enabled for now, enable when ETag caching is fleshed out
//            .addInterceptor(AddIfNoneMatchInterceptor())
//            .addNetworkInterceptor(CaptureEtagAndServe304FromCacheInterceptor())
            .build()


        private val retrofitBuilder = { baseUrl: String, okHttp: OkHttpClient ->
            val json = Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                /* this or [ChessSearchRequest] has to have every keys serialized */
            }
            val contentType = "application/json".toMediaType()
            Retrofit.Builder().baseUrl(baseUrl)
                .client(okHttp)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
        }
        val defaultRetrofit: Retrofit = retrofitBuilder(CHESS_API_URL, defaultOkHttp)

        private val buildService: Retrofit.() -> ChessApiService =
            { retrofit: Retrofit -> retrofit.create(ChessApiService::class.java) }
        val defaultService: ChessApiService = defaultRetrofit.buildService()

        val defaultInstance = ChessApiClient()
    }

    val baseUrl: String
    val service: ChessApiService

    @JvmOverloads
    constructor(
        baseUrl: String = CHESS_API_URL,
        okHttp: OkHttpClient = defaultOkHttp,
    ) {
        this.baseUrl = baseUrl
        val retrofit = retrofitBuilder(baseUrl, okHttp)
        service = retrofit.buildService()

    }

    constructor(
        retrofit: Retrofit = defaultRetrofit
    ) {
        baseUrl = retrofit.baseUrl().toString()
        service = retrofit.buildService()
    }


    @Throws(ChessApiException::class)
    suspend fun <T> execute(get: suspend (ChessApiService) -> T): T {
        try {
            return get(service)
        } catch (e: Exception) {
            throw ChessApiException.build(e)
        }
    }

    // kotlin interface

    override suspend fun getLeaderboards(): Leaderboards = execute { it.leaderboards() }
    override suspend fun getPlayer(username: String): Player = execute { it.player(username) }
    override suspend fun getPlayerStats(username: String): PlayerStats =
        execute { it.playerStats(username) }

    override suspend fun getCountry(code: String): CountryInfo = execute { it.country(code) }
    override suspend fun getCountryByUrl(url: String): CountryInfo =
        execute { it.countryByUrl(url) }

    override suspend fun searchPlayers(prefix: String): List<ChessSearchItem> =
        execute { it.searchUsername(ChessSearchRequest(prefix)).suggestions }

    suspend fun searchPlayers(request: ChessSearchRequest): List<ChessSearchItem> =
        execute { it.searchUsername(request).suggestions }


    // deprecated

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    fun close() = scope.cancel()

    // ok
    private fun <T> getAsync(get: suspend CoroutineScope.() -> T): CompletableFuture<T> =
        scope.future(block = get)


    @WorkerThread
    private fun <T> getSync(get: suspend CoroutineScope.() -> T): T =
        runBlocking(scope.coroutineContext, get)

    // Public synchronous functions for Java

    @Deprecated(
        "use ChessRepository instead"
    )
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerSync(username: String): Player = getSync { getPlayer(username) }

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerStatsSync(username: String): PlayerStats = getSync { getPlayerStats(username) }

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getLeaderboardsSync(): Leaderboards = getSync { getLeaderboards() }


    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getCountryByUrlSync(url: String): CountryInfo = getSync { getCountryByUrl(url) }

    @Deprecated("use ChessRepository instead. Direct country fetching by code is discouraged.")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getCountrySync(code: String): CountryInfo = getSync { getCountry(code) }


    // Java-friendly async wrappers (recommended for UI)

    @Deprecated("use ChessRepository instead")
    fun getPlayerAsync(username: String): CompletableFuture<Player> =
        getAsync { getPlayer(username) }

    @Deprecated("use ChessRepository instead")
    fun getPlayerStatsAsync(username: String): CompletableFuture<PlayerStats> =
        getAsync { getPlayerStats(username) }

    @Deprecated("use ChessRepository instead")
    fun getLeaderboardsAsync(): CompletableFuture<Leaderboards> = getAsync { getLeaderboards() }

    @Deprecated("use ChessRepository instead")
    fun getCountryByUrlAsync(url: String): CompletableFuture<CountryInfo> =
        getAsync { getCountryByUrl(url) }

    @Deprecated("use ChessRepository instead")
    @Throws(ChessApiException::class)
    fun searchPlayersAsync(username: String): CompletableFuture<List<ChessSearchItem>> =
        getAsync { searchPlayers(username) }
}