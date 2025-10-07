package com.ldlda.chesscom_stats.api.data.search

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * These are the keys i see in a request. i want to match it as much as possible; hence the EncodeDefaults.
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class ChessSearchRequest(
    @EncodeDefault
    var prefix: String,
    @EncodeDefault
    val boostUsername: Boolean = true,
    @EncodeDefault
    val exactUsernameFirst: Boolean = true,
    @EncodeDefault
    val friendsLimit: Int = 6,
    @EncodeDefault
    val includeFriends: Boolean = true,
    @EncodeDefault
    val includeSuggestions: Boolean = true,
    @EncodeDefault
    val suggestionsLimit: Int = 100,
)