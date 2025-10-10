package com.ldlda.chesscom_stats.api.repository

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.playerstats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import java.net.URI

/**
 * Basic implementation of [ChessRepository] that delegates all API calls to the provided [ChessApiClient].
 * This implementation does not perform any caching or additional processing.
 */
open class ChessRepositoryImpl(val client: ChessApiClient) : ChessRepository {
    override suspend fun getPlayer(username: String): Player =
        client.getPlayer(username)

    override suspend fun getPlayerStats(username: String): PlayerStats =
        client.getPlayerStats(username)

    override suspend fun getLeaderboards(): Leaderboards = client.getLeaderboards()

    override suspend fun getCountryByUrl(countryUrl: URI): CountryInfo =
        client.getCountryByUrl(countryUrl.toString())

    override suspend fun searchPlayers(prefix: String): List<SearchItem> =
        client.searchPlayers(prefix)
}