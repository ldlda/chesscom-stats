package com.ldlda.chesscom_stats.api.fetch

import androidx.annotation.WorkerThread
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
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException
import java.util.concurrent.CompletableFuture

open class ChessApiClient(
    val service: ChessApiService = run {
        val baseUrl = "https://api.chess.com/"
        val json = Json { ignoreUnknownKeys = true }

        val contentType = "application/json".toMediaType()

        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType)).build()
            .create(ChessApiService::class.java)
    }
) {
    suspend fun <T> execute(get: suspend ChessApiService.() -> T): T {
        try {
            return service.get()
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

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    fun close() = scope.cancel()

    // ok
    fun <T> getAsync(get: suspend ChessApiService.() -> T): CompletableFuture<T> =
        scope.future { execute(get) }


    @WorkerThread
    fun <T> getSync(get: suspend ChessApiService.() -> T): T =
        runBlocking(Dispatchers.IO) { execute(get) }

    // Public synchronous functions for Java

    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayer(username: String): Player {
        return getSync { getPlayer(username) }
    }


    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerStats(username: String): PlayerStats {
        return getSync { getPlayerStats(username) }
    }

    // Java-friendly async wrappers (recommended for UI)

    fun getPlayerAsync(username: String): CompletableFuture<Player> =
        getAsync { getPlayer(username) }


    fun getPlayerStatsAsync(username: String): CompletableFuture<PlayerStats> =
        getAsync { getPlayerStats(username) }


    constructor(baseUrl: String) : this(
        kotlin.run {
            val json = Json { ignoreUnknownKeys = true }

            val contentType = "application/json".toMediaType()

            Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(json.asConverterFactory(contentType)).build()
                .create(ChessApiService::class.java)
        })
}


object DefaultChessApi : ChessApiClient()

interface ChessApiService {
    @GET("pub/player/{username}")
    suspend fun getPlayer(@Path("username") username: String): Player

    @GET("pub/player/{username}/stats")
    suspend fun getPlayerStats(@Path("username") username: String): PlayerStats
}

// Sealed class hierarchy for Chess.com API exceptions.
sealed class ChessApiException(message: String?, cause: Throwable?) : Exception(message, cause) {
    class NotFound(message: String?, cause: HttpException) : ChessApiException(message, cause)
    class Gone(message: String?, cause: HttpException) : ChessApiException(message, cause)
    class TooManyRequests(message: String?, cause: HttpException) :
        ChessApiException(message, cause)

    class Redirected(message: String?, cause: HttpException) : ChessApiException(message, cause)
    data class Internal(
        val code: Int, override val message: String?, override val cause: HttpException
    ) : ChessApiException(message, cause)

    class Network(message: String?, cause: IOException) : ChessApiException(message, cause)
    class Serialization(message: String?, cause: SerializationException) :
        ChessApiException(message, cause)

    class Other(message: String?, cause: Throwable?) : ChessApiException(message, cause)
}