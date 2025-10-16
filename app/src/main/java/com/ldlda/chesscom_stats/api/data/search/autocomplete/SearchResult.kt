package com.ldlda.chesscom_stats.api.data.search.autocomplete

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * These are what i see in a response.
 *
 * This can change any moment; as well as the [request][SearchRequest]
 * and [endpoint][com.ldlda.chesscom_stats.api.fetch.PrivateApiService.autocompleteUsername].
 */
@Serializable
data class SearchResult(
    val suggestions: List<SearchItem> = emptyList()
) {
    companion object {
        private val jsonFormat =
            Json { ignoreUnknownKeys = true; prettyPrint = true; encodeDefaults = true }

        @JvmStatic
        fun fromJSON(jsonString: String): SearchResult {
            return jsonFormat.decodeFromString(jsonString)
        }
    }

    fun toJSON() = jsonFormat.encodeToString(this)
}


