package com.ldlda.chesscom_stats.api.data.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * These are what i see in a response.
 *
 * This can change any moment; as well as the [request][ChessSearchRequest]
 * and [endpoint][com.ldlda.chesscom_stats.api.fetch.ChessApiService.searchUsername].
 */
@Serializable
data class ChessSearchResult(
    val suggestions: List<ChessSearchItem> = emptyList()
) {
    companion object {
        private val jsonFormat =
            Json { ignoreUnknownKeys = true; prettyPrint = true; encodeDefaults = true }

        @JvmStatic
        fun fromJSON(jsonString: String): ChessSearchResult {
            return jsonFormat.decodeFromString(jsonString)
        }
    }

    fun toJSON() = jsonFormat.encodeToString(this)
}


