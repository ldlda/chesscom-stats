package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.Leaderboards
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.ChessSearchRequest
import com.ldlda.chesscom_stats.api.data.search.ChessSearchResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface ChessApiService {
    @GET("pub/player/{username}")
    suspend fun player(@Path("username") username: String): Player

    @GET("pub/player/{username}/stats")
    suspend fun playerStats(@Path("username") username: String): PlayerStats

    @GET("pub/leaderboards")
    suspend fun leaderboards(): Leaderboards

    @GET("pub/country/{code}")
    suspend fun country(@Path("code") countryCode: String): CountryInfo

    @GET
    suspend fun countryByUrl(@Url url: String): CountryInfo

    // This (or something of the sort) must be present or else the endpoint wont work
    @Headers("Content-Type: application/json")
    /**
     * this is not an official endpoint. it can break at any point.
     */
    @POST("https://www.chess.com/service/friends-search/idl/chesscom.friends_search.v1.FriendsSearchService/Autocomplete")
    suspend fun searchUsername(@Body searchRequest: ChessSearchRequest): ChessSearchResult
}