package com.ldlda.chesscom_stats.api.data.player

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ldlda.chesscom_stats.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Status(@param:DrawableRes val iconRes: Int, @param:StringRes val textRes: Int) {
    @SerialName("closed")
    Closed(R.drawable.ic_closed_acc, R.string.closed),

    @SerialName("closed:fair_play_violations")
    Banned(R.drawable.ic_banned_acc, R.string.banned),

    @SerialName("basic")
    Basic(R.drawable.ic_basic_acc, R.string.basic_acc),

    @SerialName("premium")
    Premium(
        R.drawable.ic_premium_acc, R.string.premium,
    ),

    @SerialName("mod")
    Mod(R.drawable.ic_mod_acc, R.string.mod),

    @SerialName("staff")
    Staff(R.drawable.ic_staff, R.string.staff)
}