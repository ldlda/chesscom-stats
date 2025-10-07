package com.ldlda.chesscom_stats.api.data.playergames

import kotlinx.serialization.Serializable

@Serializable
data class MonthlyArchive(val games: List<Game> = emptyList())