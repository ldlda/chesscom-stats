package com.ldlda.chesscom_stats.api.data.playerstats

import kotlinx.serialization.Serializable

@Serializable
data class Tactics(val highest: RatingDate, val lowest: RatingDate)