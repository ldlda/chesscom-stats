package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyGame
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import okhttp3.HttpUrl

interface ChessApiBackend {
    suspend fun getLeaderboards(): Leaderboards
    suspend fun getPlayer(username: String): Player
    suspend fun getPlayerStats(username: String): PlayerStats
    suspend fun getCountryByUrl(url: String): CountryInfo
    suspend fun getMonthlyArchivesList(username: String): List<HttpUrl>
    suspend fun getMonthlyArchives(username: String, year: Int, month: Int): List<MonthlyGame>

    suspend fun getMonthlyArchivesByUrl(url: String): List<MonthlyGame>

    /** for searchplayers (but they send everything) */
    suspend fun getCountry(code: String): CountryInfo
    suspend fun searchPlayers(prefix: String): List<SearchItem>


}
