package com.ldlda.chesscom_stats.api.data.playerstats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PuzzleRush(val best: Score? = null) {
    @Serializable
    data class Score(
        @SerialName("total_attempts")
        val totalAttempts: Int,
        val score: Int,
    )
}