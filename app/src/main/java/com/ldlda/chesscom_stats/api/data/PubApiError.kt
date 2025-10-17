package com.ldlda.chesscom_stats.api.data

import kotlinx.serialization.Serializable

@Serializable
data class PubApiError(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null,
) /*{
    companion object {
        // will 100% null. it was eaten.
//        @JvmStatic
//        fun message(e: HttpException): PubApiError? =
//            e.response()?.errorBody()?.string()?
        //            .also { Log.d("PubApiError.Companion", "message: $it") }
        //            ?.let { ChessApiClient.json.decodeFromString(it) }
    }
}*/
