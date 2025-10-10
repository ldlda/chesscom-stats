@file:UseSerializers(InstantEpochSecondSerializer::class, URISerializer::class)

package com.ldlda.chesscom_stats.api.data.playerstats

import com.ldlda.chesscom_stats.api.data.timeclass.Class
import com.ldlda.chesscom_stats.api.data.timeclass.Time
import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.URISerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json

@Serializable
data class PlayerStats(
    @SerialName("chess_daily")
    val daily: Stats<Daily>? = null,

    @SerialName("chess960_daily")
    val daily960: Stats<Daily>? = null,

    @SerialName("chess_rapid")
    val rapid: Stats<Game>? = null,

    @SerialName("chess_bullet")
    val bullet: Stats<Game>? = null,

    @SerialName("chess_blitz")
    val blitz: Stats<Game>? = null,

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

    fun scoreTimeClass(time: Time, classType: Class) = when (time to classType) {
        (Time.Daily to Class.Chess) -> daily
        (Time.Rapid to Class.Chess) -> rapid
        (Time.Bullet to Class.Chess) -> bullet
        (Time.Blitz to Class.Chess) -> blitz
        (Time.Daily to Class.Chess960) -> daily960
        else -> null
    }
}

