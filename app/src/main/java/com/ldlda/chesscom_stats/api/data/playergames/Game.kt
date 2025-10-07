@file:UseSerializers(InstantEpochSecondSerializer::class, URISerializer::class)

package com.ldlda.chesscom_stats.api.data.playergames

import com.ldlda.chesscom_stats.utils.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.utils.serialize.tostring.URISerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URI
import java.time.Instant

@Serializable

data class Game(
    val white: Player,
    val black: Player,
    val accuracies: Accuracies,
    val url: URI,

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
        val playerAPIEndpoint: URI,
        @SerialName("rating")
        val ratingThen: Int,
        val result: GameResultEnum = GameResultEnum.Lose
    )

    @Serializable
    data class Accuracies(
        val white: Double,
        val black: Double
    )
// double: kotlin.Double is the new float: kotlin.Float
}

