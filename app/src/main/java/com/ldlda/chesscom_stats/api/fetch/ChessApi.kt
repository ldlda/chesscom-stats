package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException

object ChessApi {
    private const val BASE_URL = "https://api.chess.com/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val contentType = "application/json".toMediaType()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    val service: ChessApiService by lazy {
        retrofit.create(ChessApiService::class.java)
    }

    suspend fun getPlayer(username: String): Result<Player> {
        return try {
            val res = service.getPlayer(username)
            Result.success(res)
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> Result.failure(ChessApiException.NotFound(e.message, e))
                410 -> Result.failure(ChessApiException.Gone(e.message, e))
                429 -> Result.failure(ChessApiException.TooManyRequests(e.message, e))
                in 300..399 -> Result.failure(ChessApiException.Redirected(e.message, e))
                else -> Result.failure(ChessApiException.Internal(e.code(), e.message, e))
            }
        } catch (e: IOException) {
            Result.failure(ChessApiException.Network(e.message, e))
        } catch (e: Exception) {
            Result.failure(ChessApiException.Other(e.message, e))
        }

    //        TODO("what the hell do i do here")
    }
}


interface ChessApiService {
    @GET("pub/player/{username}")
    suspend fun getPlayer(@Path("username") username: String): Player

    @GET("pub/player/{username}/stats")
    suspend fun getPlayerStats(@Path("username") username: String): PlayerStats
}

// what??????
sealed class ChessApiException(message: String?, cause: Throwable?) : Throwable(message, cause) {
    class NotFound(message: String?, cause: HttpException) : ChessApiException(message, cause)
    class Gone(message: String?, cause: HttpException) : ChessApiException(message, cause)
    class TooManyRequests(message: String?, cause: HttpException) : ChessApiException(message, cause)
    class Redirected(message: String?, cause: HttpException) : ChessApiException(message, cause)
    data class Internal(val code: Int, override val message: String?, override val cause: HttpException) : ChessApiException(message, cause)
    class Network(message: String?, cause: IOException) : ChessApiException(message, cause)
    class Other(message: String?, cause: Throwable?) : ChessApiException(message, cause)
}