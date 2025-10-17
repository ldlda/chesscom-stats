package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.club.Club
import com.ldlda.chesscom_stats.api.data.country.CountryClubs
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.country.CountryPlayers
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.clubs.PlayerClubs
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyArchives
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyGameList
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.puzzle.Puzzle
import okhttp3.HttpUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


/**
 * this uses the minimally processed data in and produces the minimally processed data out
 *
 * (for example [monthlyArchives] no one cant tell you you cant supply a random ahh string)
 */
interface ChessApiService {
    @GET("player/{username}")
    suspend fun player(@Path("username") username: String): Response<Player>

    @GET("player/{username}/stats")
    suspend fun playerStats(@Path("username") username: String): Response<PlayerStats>

    @GET("player/{username}/clubs")
    suspend fun playerClubs(@Path("username") username: String): Response<PlayerClubs>

    @GET("leaderboards")
    suspend fun leaderboards(): Response<Leaderboards>


    //region Cuntry

    @GET("country/{code}")
    suspend fun country(@Path("code") countryCode: String): Response<CountryInfo>

    @GET
    suspend fun country(@Url url: HttpUrl): Response<CountryInfo>

    @GET("country/{code}/players")
    suspend fun countryPlayers(@Path("code") countryCode: String): Response<CountryPlayers>

    @GET("country/{code}/clubs")
    suspend fun countryClubs(@Path("code") countryCode: String): Response<CountryClubs>

    //endregion

    @GET("player/{username}/games/archives")
    suspend fun monthlyArchivesList(@Path("username") username: String): Response<MonthlyArchives>

    @GET("player/{username}/games/{year}/{month}")
    suspend fun monthlyArchives(
        @Path("username") username: String,
        @Path("year") year: String,
        @Path("month") month: String
    ): Response<MonthlyGameList>

    @GET
    suspend fun monthlyArchives(@Url url: HttpUrl): Response<MonthlyGameList>

    @GET
    suspend fun club(@Url url: HttpUrl): Response<Club>


    @GET("club/{nameId}")
    suspend fun club(@Path("nameId") nameId: String): Response<Club>

    @GET("puzzle")
    suspend fun dailyPuzzle(): Response<Puzzle>

    @GET("puzzle/random")
    suspend fun randomPuzzle(): Response<Puzzle>

    // not supporting live/base/increment because if you query hikaru it will 503
    // there are more that hits 503
}