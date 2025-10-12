package com.ldlda.chesscom_stats.api.fetch

import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchRequest
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchResult
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PrivateApiService {
    // may pluck this into a separate interface
    // This (or something of the sort) must be present or else the endpoint wont work
    @Headers("Content-Type: application/json")
    /**
     * this is not an official endpoint. it can break at any point.
     */
    @POST("https://www.chess.com/service/friends-search/idl/chesscom.friends_search.v1.FriendsSearchService/Autocomplete")
    suspend fun autocompleteUsername(@Body searchRequest: SearchRequest): SearchResult

}