package com.ldlda.chesscom_stats.api.fetch

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

// Sealed class hierarchy for Chess.com API exceptions.
sealed class ChessApiException(message: String?, cause: Throwable?) : Exception(message, cause) {
    class NotFound(message: String?, cause: HttpException) : ChessApiException(message, cause)
    class Gone(message: String?, cause: HttpException) : ChessApiException(message, cause)
    class TooManyRequests(message: String?, cause: HttpException) :
        ChessApiException(message, cause)

    class Redirected(message: String?, cause: HttpException) : ChessApiException(message, cause)
    data class Internal(
        val code: Int, override val message: String?, override val cause: HttpException
    ) : ChessApiException(message, cause)

    class Network(message: String?, cause: IOException) : ChessApiException(message, cause)
    class Serialization(message: String?, cause: SerializationException) :
        ChessApiException(message, cause)

    class Other(message: String?, cause: Throwable?) : ChessApiException(message, cause)
}