package com.ldlda.chesscom_stats.api.data.player

import kotlinx.serialization.Serializable

@Serializable
enum class Title {
    GM, WGM, IM, WIM, FM, WFM, NM, WNM, CM, WCM
}