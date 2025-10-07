package com.ldlda.chesscom_stats.api.data.search

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.utils.serialize.tostring.InstantParseSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ChessSearchItem(
    val userView: ChessComUserView,
    val country: CountryInfo,
    @Serializable(with = InstantParseSerializer::class)
    val lastLoginDate: Instant,
    val bestRating: Int? = null,
)