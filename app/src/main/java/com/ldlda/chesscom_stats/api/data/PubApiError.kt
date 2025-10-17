package com.ldlda.chesscom_stats.api.data

import kotlinx.serialization.Serializable

@Serializable
data class PubApiError(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null,
)