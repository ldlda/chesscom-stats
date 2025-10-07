package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.playergames.Game
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.playerstats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.ChessSearchItem
import java.net.URI

interface ChessApiBackend {
    suspend fun getLeaderboards(): Leaderboards
    suspend fun getPlayer(username: String): Player
    suspend fun getPlayerStats(username: String): PlayerStats
    suspend fun getCountryByUrl(url: String): CountryInfo
    suspend fun getMonthlyArchivesList(username: String): List<URI>
    suspend fun getMonthlyArchives(username: String, year: Int, month: Int): List<Game>

    suspend fun getMonthlyArchivesByUrl(url: String): List<Game>
    /** for searchplayers (but they send everything) */
    suspend fun getCountry(code: String): CountryInfo
    suspend fun searchPlayers(prefix: String): List<ChessSearchItem>


}
