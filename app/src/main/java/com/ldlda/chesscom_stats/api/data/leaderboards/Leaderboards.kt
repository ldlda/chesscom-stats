@file:UseSerializers(InstantEpochSecondSerializer::class, URISerializer::class)

package com.ldlda.chesscom_stats.api.data.leaderboards

import com.ldlda.chesscom_stats.utils.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.utils.serialize.tostring.URISerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class Leaderboards(
    @SerialName("live_blitz")
    val blitz: List<LeaderboardEntry> = emptyList(),
    @SerialName("live_bullet")
    val bullet: List<LeaderboardEntry> = emptyList(),
    @SerialName("live_rapid")
    val rapid: List<LeaderboardEntry> = emptyList(),
    val tactics: List<LeaderboardEntry> = emptyList(),
)

