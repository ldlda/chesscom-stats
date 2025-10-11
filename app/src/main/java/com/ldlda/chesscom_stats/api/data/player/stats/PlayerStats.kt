@file:UseSerializers(InstantEpochSecondSerializer::class, HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.player.stats

import com.ldlda.chesscom_stats.api.data.timeclass.Rule
import com.ldlda.chesscom_stats.api.data.timeclass.Time
import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json

@Serializable
data class PlayerStats(
    @SerialName("chess_daily")
    val daily: Stats<DailyRecord>? = null,

    @SerialName("chess960_daily")
    val daily960: Stats<DailyRecord>? = null,

    @SerialName("chess_rapid")
    val rapid: Stats<Record>? = null,

    @SerialName("chess_bullet")
    val bullet: Stats<Record>? = null,

    @SerialName("chess_blitz")
    val blitz: Stats<Record>? = null,

    val fide: Int = 0,

    val tactics: Tactics? = null,

    @SerialName("puzzle_rush")
    val puzzleRush: PuzzleRush? = null,
) {
    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true; prettyPrint = true }

        @JvmStatic
        fun fromJSON(jsonString: String): PlayerStats = jsonFormat.decodeFromString(jsonString)
    }

    fun toJSON() = jsonFormat.encodeToString(this)

    fun scoreTimeClass(time: Time, classType: Rule) = when (time to classType) {
        (Time.Daily to Rule.Chess) -> daily
        (Time.Rapid to Rule.Chess) -> rapid
        (Time.Bullet to Rule.Chess) -> bullet
        (Time.Blitz to Rule.Chess) -> blitz
        (Time.Daily to Rule.Chess960) -> daily960
        else -> null
    }
}

