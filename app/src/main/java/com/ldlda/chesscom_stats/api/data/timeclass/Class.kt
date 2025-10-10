package com.ldlda.chesscom_stats.api.data.timeclass

enum class Class {
    Crazyhouse,
    Chess,
    Chess960,
    Bughouse,
    Threecheck,
    Kingofthehill,
    ;

    companion object {
        fun goodValueOf(value: String) =
            Class.valueOf(value.lowercase().replaceFirstChar { it.titlecase() })
    }
}