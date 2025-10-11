package com.ldlda.chesscom_stats.api.data.player.stats

// abc or interface all is good
abstract class BaseRecord() {
    abstract val win: Int
    abstract val draw: Int
    abstract val loss: Int
}