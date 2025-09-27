package com.ldlda.chesscom_stats.api.repository

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.Leaderboards
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.ChessSearchItem
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import java.net.URI

// bare ass chess repo
open class ChessRepositoryImpl(val client: ChessApiClient) : ChessRepository {
    override suspend fun getPlayer(username: String): Player =
        client.getPlayer(username)

    override suspend fun getPlayerStats(username: String): PlayerStats =
        client.getPlayerStats(username)

    override suspend fun getLeaderboards(): Leaderboards = client.getLeaderboards()

    override suspend fun getCountry(countryUrl: URI): CountryInfo =
        client.getCountryByUrl(countryUrl.toString())

    override suspend fun searchPlayers(prefix: String): List<ChessSearchItem> =
        client.searchPlayers(prefix)
}