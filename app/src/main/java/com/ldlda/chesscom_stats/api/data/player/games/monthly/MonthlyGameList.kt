package com.ldlda.chesscom_stats.api.data.player.games.monthly

import com.ldlda.chesscom_stats.api.data.player.games.BaseGameList
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyGameList(override val games: List<MonthlyGame> = emptyList()) : BaseGameList()