package com.ldlda.chesscom_stats.api.data.player.games

import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyGame

abstract class BaseGameList() {
    abstract val games: List<MonthlyGame>
}