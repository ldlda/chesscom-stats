package com.ldlda.chesscom_stats.api.data.player.stats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyRecord(
    override val win: Int,
    override val draw: Int,
    override val loss: Int,
    @SerialName("time_per_move") val timePerMove: UInt,
    @SerialName("timeout_percent") val timeoutPercent: UInt,
) : BaseRecord()