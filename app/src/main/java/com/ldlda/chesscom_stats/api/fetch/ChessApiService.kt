package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.Leaderboards
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ChessApiService {
    @GET("pub/player/{username}")
    suspend fun getPlayer(@Path("username") username: String): Player

    @GET("pub/player/{username}/stats")
    suspend fun getPlayerStats(@Path("username") username: String): PlayerStats

    @GET("pub/leaderboards")
    suspend fun getLeaderboards(): Leaderboards

    @GET("pub/country/{code}")
    suspend fun getCountry(@Path("code") countryCode: String): CountryInfo

    @GET
    suspend fun getCountryByUrl(@Url url: String): CountryInfo
}