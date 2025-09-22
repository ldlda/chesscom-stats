package com.ldlda.chesscom_stats.api.repository

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.Leaderboards
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats

interface ChessRepository {
    suspend fun getPlayer(username: String): Player
    suspend fun getPlayerStats(username: String): PlayerStats

    suspend fun getLeaderboards(): Leaderboards

    suspend fun getCountry(countryUrl: java.net.URI): CountryInfo
}