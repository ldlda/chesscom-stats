package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.Leaderboards
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats

interface ChessApiBackend {
    suspend fun getLeaderboards(): Leaderboards
    suspend fun getPlayer(username: String): Player
    suspend fun getPlayerStats(username: String): PlayerStats
    suspend fun getCountryByUrl(url: String): CountryInfo

}
