@file:UseSerializers(HttpUrlSerializer::class, InstantEpochSecondSerializer::class)

package com.ldlda.chesscom_stats.api.data.player.games.monthly

import com.ldlda.chesscom_stats.api.data.player.games.GameResult
import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl
import java.time.Instant

// specifically Game from MonthlyArchives. there are multiple shapes of ts? WHY?
@Serializable
data class MonthlyGame(
    val white: Player,
    val black: Player,
    val accuracies: Accuracies,
    val url: HttpUrl,

    val fen: String,
    val pgn: String,
//    val tcn: String, // proprie shit get out
    @SerialName("initial_setup")
    val initialSetup: String? = null,

    @SerialName("start_time")
    val startTime: Instant,
    @SerialName("end_time")
    val endTime: Instant? = null,
    @SerialName("time_control")
    val timeControl: String,

    val rated: Boolean? = null,
    val rules: String,
    val timeClass: String? = null,
    val eco: String? = null,
) {
    @Serializable
    data class Player(
        val username: String,
        @SerialName("@id")
        val playerAPIEndpoint: HttpUrl,
        @SerialName("rating")
        val ratingThen: Int,
        val result: GameResult = GameResult.Lose
    )

    @Serializable
    data class Accuracies(
        val white: Double,
        val black: Double
    )
// double: kotlin.Double is the new float: kotlin.Float
}