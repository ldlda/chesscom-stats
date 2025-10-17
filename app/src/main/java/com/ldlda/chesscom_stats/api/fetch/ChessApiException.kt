package com.ldlda.chesscom_stats.api.fetch

import android.util.Log
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

// Sealed class hierarchy for Chess.com API exceptions.
// the fun thing about sealed class is that you can switch case on it.
sealed class ChessApiException(message: String?, cause: Throwable?) : Exception(message, cause) {
    class NotFound(message: String?, cause: HttpException?) : ChessApiException(message, cause)
    class Gone(message: String?, cause: HttpException?) : ChessApiException(message, cause)
    class TooManyRequests(message: String?, cause: HttpException?) :
        ChessApiException(message, cause)

    class Redirected(message: String?, cause: HttpException?) : ChessApiException(message, cause)
    data class Internal(
        val code: Int, override val message: String?, override val cause: HttpException?
    ) : ChessApiException(message, cause)

    class Network(message: String?, cause: IOException) : ChessApiException(message, cause)
    class Serialization(message: String?, cause: SerializationException) :
        ChessApiException(message, cause)

    class Other(message: String?, cause: Throwable?) : ChessApiException(message, cause)

    companion object {
        private const val TAG = "ChessApiException"

        /**
         * Extracts the Chess.com API error message from HttpException.
         * First tries the custom header added by ChessApiErrorInterceptor,
         * then falls back to the HTTP message.
         */
        private fun extractErrorMessage(e: HttpException): String? {
            // Try to get the parsed error message from the header
            val headerMessage =
                e.response()?.headers()?.get(ChessApiErrorInterceptor.HEADER_CHESS_API_ERROR)
            return headerMessage?.also {
                Log.d(
                    TAG,
                    "extractErrorMessage: $it"
                ); println("$it is Error")
            } ?: e.message()
        }

        fun build(e: Exception) = when (e) {
            is ChessApiException -> e
            is HttpException -> {
                val errorMessage = extractErrorMessage(e)
                when (e.code()) {
                    404 -> NotFound(errorMessage, e)
                    410 -> Gone(errorMessage, e)
                    429 -> TooManyRequests(errorMessage, e)
                    // never happens (because okhttp good)
                    in 300..399 -> Redirected(errorMessage, e)
                    else -> Internal(e.code(), errorMessage, e)
                }
            }

            is IOException -> Network(e.message, e)

            // this happens when type T doesn't work with what [get] got
            is SerializationException -> Serialization(e.message, e)

            else -> Other(e.message, e)
        }
    }

}