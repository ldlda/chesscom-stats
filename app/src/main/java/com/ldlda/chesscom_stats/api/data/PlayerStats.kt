package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.api.fetch.ChessApi
import com.ldlda.chesscom_stats.api.fetch.ChessApiException
import com.ldlda.chesscom_stats.utils.parse.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.Instant

@Serializable
data class PlayerStats(
    @SerialName("chess_daily")
    val daily: SimpleStat? = null,

    @SerialName("chess_rapid")
    val rapid: SimpleStat? = null,

    @SerialName("chess_bullet")
    val bullet: SimpleStat? = null,

    @SerialName("chess_blitz")
    val blitz: SimpleStat? = null,

    val fide: Int = 0,

    val tactics: Tactics? = null,

    @SerialName("puzzle_rush")
    val puzzleRush: PuzzleRush? = null,
) {
    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true }

        @JvmStatic
        fun fromJSON(jsonString: String): PlayerStats =
            jsonFormat.decodeFromString(serializer(), jsonString)
    }
}

@Serializable
data class SimpleStat(
    val last: RatingDate,
    val best: RatingDate? = null,
    val record: GameRecord,
)

@Serializable
data class Tactics(
    val highest: RatingDate,
    val lowest: RatingDate,
)

@Serializable
data class PuzzleRush(
    val best: PuzzleRushScore
)

@Serializable
data class PuzzleRushScore(
    @SerialName("total_attempts")
    val totalAttempts: Int,
    val score: Int,
)

@Serializable
data class RatingDate(
    val rating: Int,
    @Serializable(with = InstantSerializer::class)
    val date: Instant,
    // val rd: Int?,
)

@Serializable
data class GameRecord(
    val win: Int,
    val draw: Int,
    val loss: Int,
//    val time_per_move: UInt?,
//    val timeout_percent: UInt?,
)