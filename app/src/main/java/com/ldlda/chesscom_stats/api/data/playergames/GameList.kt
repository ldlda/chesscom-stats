package com.ldlda.chesscom_stats.api.data.playergames

import kotlinx.serialization.Serializable

@Serializable
data class GameList(val games: List<Game> = emptyList())