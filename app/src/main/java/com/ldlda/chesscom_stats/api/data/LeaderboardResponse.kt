package com.ldlda.chesscom_stats.api.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardResponse(
    @SerialName("live_blitz")
    val liveBlitz: List<PlayerLeaderboardEntry> = emptyList()
)

@Serializable
data class PlayerLeaderboardEntry(
    val username: String,
    val avatar: String? = null,
    val score: Int = 0,
    val rank: Int = 0
)

