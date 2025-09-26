package com.ldlda.chesscom_stats.api.fetch

import androidx.annotation.WorkerThread
import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.Leaderboards
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException
import java.util.concurrent.CompletableFuture

open class ChessApiClient(
    val baseUrl: String = "https://api.chess.com/",
    val okHttp: OkHttpClient = OkHttpClient.Builder()
//            .addInterceptor(AddIfNoneMatchInterceptor())
//            .addNetworkInterceptor(CaptureEtagAndServe304FromCacheInterceptor())
        .build(),
    val retrofit: Retrofit = run {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttp)
            .addConverterFactory(json.asConverterFactory(contentType)).build()
    },
    val service: ChessApiService = retrofit.create(ChessApiService::class.java)

) : ChessApiBackend {
    suspend fun <T> execute(get: suspend (ChessApiService) -> T): T {
        try {
            return get(service)
        } catch (e: HttpException) {
            throw when (e.code()) {
                404 -> ChessApiException.NotFound(e.message(), e)
                410 -> ChessApiException.Gone(e.message(), e)
                429 -> ChessApiException.TooManyRequests(e.message(), e)
                in 300..399 -> ChessApiException.Redirected(e.message(), e)
                else -> ChessApiException.Internal(e.code(), e.message(), e)
            }
        } catch (e: IOException) {
            throw (ChessApiException.Network(e.message, e))
        } catch (e: SerializationException) {
            // this happens when type T doesn't work with what [get] got
            throw ChessApiException.Serialization(e.message, e)
        } catch (e: Exception) {
            throw (ChessApiException.Other(e.message, e))
        }
    }

    // kotlin interface

    override suspend fun getLeaderboards(): Leaderboards = execute { it.leaderboards() }
    override suspend fun getPlayer(username: String): Player = execute { it.player(username) }
    override suspend fun getPlayerStats(username: String): PlayerStats =
        execute { it.playerStats(username) }

    suspend fun getCountry(code: String): CountryInfo = execute { it.country(code) }
    override suspend fun getCountryByUrl(url: String): CountryInfo =
        execute { it.countryByUrl(url) }

    // deprecated

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    fun close() = scope.cancel()

    // ok
    private fun <T> getAsync(get: suspend (ChessApiService) -> T): CompletableFuture<T> =
        scope.future { execute(get) }


    @WorkerThread
    private fun <T> getSync(get: suspend (ChessApiService) -> T): T =
        runBlocking(Dispatchers.IO) { execute(get) }

    // Public synchronous functions for Java

    @Deprecated(
        "use ChessRepository instead"
    )
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerSync(username: String): Player {
        return getSync { it.player(username) }
    }

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerStatsSync(username: String): PlayerStats {
        return getSync { it.playerStats(username) }
    }

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getLeaderboardsSync(): Leaderboards {
        return getSync { it.leaderboards() }
    }

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getCountryByUrlSync(url: String): CountryInfo = getSync { it.countryByUrl(url) }

    @Deprecated("use ChessRepository instead. Direct country fetching by code is discouraged.")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getCountrySync(code: String): CountryInfo = getSync { it.country(code) }

    // Java-friendly async wrappers (recommended for UI)

    @Deprecated("use ChessRepository instead")
    fun getPlayerAsync(username: String): CompletableFuture<Player> =
        getAsync { it.player(username) }

    @Deprecated("use ChessRepository instead")
    fun getPlayerStatsAsync(username: String): CompletableFuture<PlayerStats> =
        getAsync { it.playerStats(username) }

    @Deprecated("use ChessRepository instead")
    fun getLeaderboardsAsync(): CompletableFuture<Leaderboards> = getAsync { it.leaderboards() }

    @Deprecated("use ChessRepository instead")
    fun getCountryByUrlAsync(url: String): CompletableFuture<CountryInfo> =
        getAsync { it.countryByUrl(url) }
}

object DefaultChessApi : ChessApiClient()
