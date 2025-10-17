package com.ldlda.chesscom_stats.ui.favorites

import com.ldlda.chesscom_stats.api.data.player.Title
import java.time.Instant

@JvmRecord
data class UserFavoriteModel(
    @JvmField val username: String,
    @JvmField val userId: Long,
    @JvmField val favoriteSince: Instant,
    @JvmField val title: Title?,
    @JvmField val lastLoginTime: Instant?
)
