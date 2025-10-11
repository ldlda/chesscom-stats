package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyArchives
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyGameList
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchRequest
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchResult
import com.ldlda.chesscom_stats.java_api.ClubData
import com.ldlda.chesscom_stats.java_api.PuzzleData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url
import com.ldlda.chesscom_stats.api.data.country.Clubs as CountryClubs
import com.ldlda.chesscom_stats.api.data.country.Players as CountryPlayers

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

    @GET("country/{code}/players")
    suspend fun countryPlayers(@Path("code") countryCode: String): CountryPlayers

    @GET("country/{code}/clubs")
    suspend fun ountryClubs(@Path("code") countryCode: String): CountryClubs


    @GET("player/{username}/games/archives")
    suspend fun monthlyArchivesList(@Path("username") username: String): MonthlyArchives

    @GET("player/{username}/games/{year}/{month}")
    suspend fun monthlyArchives(
        @Path("username") username: String,
        @Path("year") year: String,
        @Path("month") month: String
    ): MonthlyGameList

    @GET
    suspend fun monthlyArchivesByUrl(@Url url: String): MonthlyGameList

    @GET("club/{nameId}")
    suspend fun club(@Path("nameId") nameId: String): ClubData

    @GET("puzzle")
    suspend fun dailyPuzzle(): PuzzleData

    @GET("puzzle/random")
    suspend fun randomPuzzle(): PuzzleData

    // not supporting live/base/increment because if you query hikaru it will 503
    // there are more that hits 503
    // may pluck this into a separate interface
    // This (or something of the sort) must be present or else the endpoint wont work
    @Headers("Content-Type: application/json")
    /**
     * this is not an official endpoint. it can break at any point.
     */
    @POST("https://www.chess.com/service/friends-search/idl/chesscom.friends_search.v1.FriendsSearchService/Autocomplete")
    suspend fun autocompleteUsername(@Body searchRequest: SearchRequest): SearchResult
}