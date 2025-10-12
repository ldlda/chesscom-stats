package com.ldlda.chesscom_stats.api.repository

import com.ldlda.chesscom_stats.api.data.club.Club
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyGame
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchRequest
import okhttp3.HttpUrl

interface ChessRepository {
    suspend fun getPlayer(username: String): Player
    suspend fun getPlayerStats(username: String): PlayerStats
    suspend fun getLeaderboards(): Leaderboards
    suspend fun getCountry(countryUrl: HttpUrl): CountryInfo

    /** for searchplayers (but they send everything) */
    suspend fun getCountry(code: String): CountryInfo

    suspend fun getMonthlyArchivesList(username: String): List<HttpUrl>
    suspend fun getMonthlyArchives(username: String, year: Int, month: Int): List<MonthlyGame>
    suspend fun getMonthlyArchives(url: HttpUrl): List<MonthlyGame>
    suspend fun getCountryClubs(code: String): List<HttpUrl>

    suspend fun getClub(url: HttpUrl): Club
    suspend fun getClub(nameId: String): Club

    suspend fun searchPlayers(prefix: String): List<SearchItem>
    suspend fun searchPlayers(request: SearchRequest): List<SearchItem>
}