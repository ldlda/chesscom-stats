package com.ldlda.chesscom_stats.api.data.timeclass

enum class Time {
    Daily,
    Blitz,
    Bullet,
    Rapid,
    ;

    companion object {
        fun goodValueOf(value: String) =
            Time.valueOf(value.lowercase().replaceFirstChar { it.titlecase() })
    }
}
