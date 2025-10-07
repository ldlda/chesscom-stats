package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.playergames.MonthlyArchive
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.playergames.MonthlyArchives
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.playerstats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.ChessSearchRequest
import com.ldlda.chesscom_stats.api.data.search.ChessSearchResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface ChessApiService {
    @GET("player/{username}")
    suspend fun player(@Path("username") username: String): Player

    @GET("player/{username}/stats")
    suspend fun playerStats(@Path("username") username: String): PlayerStats

    @GET("leaderboards")
    suspend fun leaderboards(): Leaderboards

    @GET("country/{code}")
    suspend fun country(@Path("code") countryCode: String): CountryInfo

    @GET
    suspend fun countryByUrl(@Url url: String): CountryInfo

    @GET("player/{username}/games/archives")
    suspend fun monthlyArchivesList(@Path("username") username: String): MonthlyArchives

    @GET("player/{username}/games/{year}/{month}")
    suspend fun monthlyArchives(
        @Path("username") username: String,
        @Path("year") year: String,
        @Path("month") month: String
    ): MonthlyArchive

    @GET
    suspend fun monthlyArchivesByUrl(@Url url: String): MonthlyArchive

    // not supporting live/base/increment because if you query hikaru it will 503

    // This (or something of the sort) must be present or else the endpoint wont work
    @Headers("Content-Type: application/json")
    /**
     * this is not an official endpoint. it can break at any point.
     */
    @POST("https://www.chess.com/service/friends-search/idl/chesscom.friends_search.v1.FriendsSearchService/Autocomplete")
    suspend fun searchUsername(@Body searchRequest: ChessSearchRequest): ChessSearchResult
}