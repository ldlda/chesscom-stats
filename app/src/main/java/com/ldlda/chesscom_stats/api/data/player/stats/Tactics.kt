package com.ldlda.chesscom_stats.api.data.player.stats

import kotlinx.serialization.Serializable

@Serializable
data class Tactics(val highest: RatingDate, val lowest: RatingDate)