package com.ldlda.chesscom_stats.api.repository

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.playerstats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.ChessSearchItem
import java.net.URI

interface ChessRepository {
    suspend fun getPlayer(username: String): Player
    suspend fun getPlayerStats(username: String): PlayerStats
    suspend fun getLeaderboards(): Leaderboards
    suspend fun getCountryByUrl(countryUrl: URI): CountryInfo
    suspend fun searchPlayers(prefix: String): List<ChessSearchItem>
}