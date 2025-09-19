package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

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
//        return service.getPlayer(username).let { response ->
//            when {
//                response.isSuccessful -> {
//                    response.body().takeIf { it != null }
//                }
//
//                else -> {
//                    Result.failure()
//                }
//            }
//        }
        TODO("what the hell do i do here")
    }
}


interface ChessApiService {
    @GET("pub/player/{username}")
    suspend fun getPlayer(@Path("username") username: String): Response<Player>

    @GET("pub/player/{username}/stats")
    suspend fun getPlayerStats(@Path("username") username: String): Response<PlayerStats>
}
