package com.ldlda.chesscom_stats.api.data.player.stats

import kotlinx.serialization.Serializable

@Serializable
data class Record(
    override val win: Int, override val draw: Int, override val loss: Int
) : BaseRecord()