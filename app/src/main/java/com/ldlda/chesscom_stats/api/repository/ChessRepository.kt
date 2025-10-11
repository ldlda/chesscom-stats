package com.ldlda.chesscom_stats.api.repository

import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import okhttp3.HttpUrl

interface ChessRepository {
    suspend fun getPlayer(username: String): Player
    suspend fun getPlayerStats(username: String): PlayerStats
    suspend fun getLeaderboards(): Leaderboards
    suspend fun getCountryByUrl(countryUrl: HttpUrl): CountryInfo
    suspend fun searchPlayers(prefix: String): List<SearchItem>
}