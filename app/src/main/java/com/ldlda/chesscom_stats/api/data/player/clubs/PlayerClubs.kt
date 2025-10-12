package com.ldlda.chesscom_stats.api.data.player.clubs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerClubs(@SerialName("clubs") val playerClubs: List<PlayerClub> = emptyList())