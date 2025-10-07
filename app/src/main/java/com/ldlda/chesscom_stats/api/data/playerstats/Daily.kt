package com.ldlda.chesscom_stats.api.data.playerstats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Daily(
    val win: Int,
    val draw: Int,
    val loss: Int,
    @SerialName("time_per_move") val timePerMove: UInt,
    @SerialName("timeout_percent") val timeoutPercent: UInt,
)