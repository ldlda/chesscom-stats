package com.ldlda.chesscom_stats.api.repository

import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.data.PlayerStats
import com.ldlda.chesscom_stats.api.fetch.ChessApi
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient

class ChessRepositoryImpl(
    private val client: ChessApiClient = ChessApi // default
) : ChessRepository {
    override suspend fun getPlayer(username: String): Player =
        client.execute { getPlayer(username.normalize()) }

    override suspend fun getPlayerStats(username: String): PlayerStats =
        client.execute { getPlayerStats(username.normalize()) }

    private fun String.normalize(): String = trim().lowercase()
}



