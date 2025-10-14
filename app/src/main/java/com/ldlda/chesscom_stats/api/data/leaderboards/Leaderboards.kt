@file:UseSerializers(InstantEpochSecondSerializer::class, URISerializer::class)

package com.ldlda.chesscom_stats.api.data.leaderboards

import com.ldlda.chesscom_stats.api.data.timeclass.Rule
import com.ldlda.chesscom_stats.api.data.timeclass.Time
import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.URISerializer
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

    val daily: List<LeaderboardEntry> = emptyList(),

    val daily960: List<LeaderboardEntry> = emptyList(),
    @SerialName("live_bughouse")
    val bughouse: List<LeaderboardEntry> = emptyList(),
    @SerialName("live_blitz960")
    val blitz960: List<LeaderboardEntry> = emptyList(),
    @SerialName("live_threecheck")
    val threecheck: List<LeaderboardEntry> = emptyList(),
    @SerialName("live_crazyhouse")
    val crazyhouse: List<LeaderboardEntry> = emptyList(),
    @SerialName("live_kingofthehill")
    val kingofthehill: List<LeaderboardEntry> = emptyList(),

    val tactics: List<LeaderboardEntry> = emptyList(),
    val rush: List<LeaderboardEntry> = emptyList(),
    val battle: List<LeaderboardEntry> = emptyList(),
) {
    /**
     * Get leaderboard entries by time class.
     * Returns empty list if combination doesn't exist.
     *
     * Note: API doesn't tell us which time class each list is from,
     * so we have to pattern match manually. Ass design.
     */
    fun fromTimeclass(rule: Rule?, time: Time?): List<LeaderboardEntry> = when (rule to time) {
        // Standard chess time controls
        (Rule.Chess to Time.Blitz), (null to Time.Blitz) -> blitz
        (Rule.Chess to Time.Bullet), (null to Time.Bullet) -> bullet
        (Rule.Chess to Time.Rapid), (null to Time.Rapid) -> rapid
        (Rule.Chess to Time.Daily), (null to Time.Daily) -> daily

        // Chess960 variants
        (Rule.Chess960 to Time.Blitz) -> blitz960
        (Rule.Chess960 to Time.Daily) -> daily960

        // Special rule variants (all blitz)
        (Rule.Kingofthehill to Time.Blitz), (Rule.Kingofthehill to null) -> kingofthehill
        (Rule.Threecheck to Time.Blitz), (Rule.Threecheck to null) -> threecheck
        (Rule.Crazyhouse to Time.Blitz), (Rule.Crazyhouse to null) -> crazyhouse
        (Rule.Bughouse to Time.Blitz), (Rule.Bughouse to null) -> bughouse

        // whatever else this is
        (Rule.Tactics to null) -> tactics
        (Rule.Battle to null) -> battle
        (Rule.Rush to null) -> rush

        else -> emptyList()
    }
}

