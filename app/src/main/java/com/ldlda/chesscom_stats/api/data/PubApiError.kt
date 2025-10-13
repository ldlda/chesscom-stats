package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import kotlinx.serialization.Serializable
import retrofit2.HttpException

@Serializable
data class PubApiError(
    val code: Int,
    val message: String? = null,
    val status: String? = null,
) {
    companion object {
        @JvmStatic
        fun message(e: HttpException): PubApiError? =
            e.response()?.errorBody()?.string()?.let { ChessApiClient.json.decodeFromString(it) }
    }
}
