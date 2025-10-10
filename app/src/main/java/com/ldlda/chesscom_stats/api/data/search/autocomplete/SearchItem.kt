package com.ldlda.chesscom_stats.api.data.search.autocomplete

import com.ldlda.chesscom_stats.api.data.CountryInfo
import com.ldlda.chesscom_stats.util.serialize.tostring.InstantParseSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class SearchItem(
    val userView: UserView,
    val country: CountryInfo,
    @Serializable(with = InstantParseSerializer::class)
    val lastLoginDate: Instant,
    val bestRating: Int? = null,
    val ratings: List<Rating> = emptyList()
)