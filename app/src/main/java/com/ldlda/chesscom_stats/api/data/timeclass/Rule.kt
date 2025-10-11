package com.ldlda.chesscom_stats.api.data.timeclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Rule {
    @SerialName("crazyhouse")
    Crazyhouse,

    @SerialName("chess")
    Chess,

    @SerialName("chess960")
    Chess960,

    @SerialName("bughouse")
    Bughouse,

    @SerialName("threecheck")
    Threecheck,

    @SerialName("kingofthehill")
    Kingofthehill, ;

    companion object {
        fun goodValueOf(value: String) =
            Rule.valueOf(value.lowercase().replaceFirstChar { it.titlecase() })
    }
}