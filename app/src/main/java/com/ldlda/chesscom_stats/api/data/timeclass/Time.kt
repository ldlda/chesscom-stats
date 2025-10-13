package com.ldlda.chesscom_stats.api.data.timeclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Time {
    @SerialName("daily")
    Daily,

    @SerialName("blitz")
    Blitz,

    @SerialName("bullet")
    Bullet,

    @SerialName("rapid")
    Rapid,
    ;

    companion object {
        fun goodValueOf(value: String) =
            Time.valueOf(value.lowercase().replaceFirstChar { it.titlecase() })
    }
}
