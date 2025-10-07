package com.ldlda.chesscom_stats.api.data.playergames

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GameResultEnum(val score: Int) {
    // win
    @SerialName("win")
    Win(1),

    // lose
    @SerialName("resigned")
    Resigned(-1),

    @SerialName("checkmated")
    Checkmated(-1),

    @SerialName("lose")
    Lose(-1),

    @SerialName("bughousepartnerlose")
    BughousePartnerLose(-1),

    @SerialName("kingofthehill")
    KingOfTheHill(-1),

    @SerialName("threecheck")
    ThreeCheck(-1),

    @SerialName("abandoned")
    Abandoned(-1),

    // draw - other
    @SerialName("50move")
    FiftyMove(0),

    @SerialName("agreed")
    Agreed(0),

    @SerialName("insufficient")
    Insufficient(0),

    @SerialName("stalemate")
    Stalemate(0),

    @SerialName("repetition")
    Repetition(0),

    @SerialName("timevsinsufficient")
    TimeVsInsufficient(0),

    ;
}