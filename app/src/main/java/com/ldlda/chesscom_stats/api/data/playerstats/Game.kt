package com.ldlda.chesscom_stats.api.data.playerstats

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val win: Int, val draw: Int, val loss: Int
)