package com.ldlda.chesscom_stats.api.data.search.autocomplete

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * These are the keys i see in a request. i want to match it as much as possible; hence the EncodeDefaults.
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class SearchRequest(
    /**
     * this key is must for it to not error, also needs to be >= 3 chr
     */
    @EncodeDefault
    var prefix: String = "",
    // maybe this does something idk
    @EncodeDefault
    val boostUsername: Boolean = true,
    // what does ts even do bro
    @EncodeDefault
    val exactUsernameFirst: Boolean = true,
    // as were offline we dont have friends
    val friendsLimit: Int = 0,
    // no
    val includeFriends: Boolean = false,
    /**
     * this key is must for anything to appear
     */
    @EncodeDefault
    val includeSuggestions: Boolean = true,
    /**
     * this is used to somewhat control the length of responses
     * 5 by default, if we dont have this key explicitly
     */
    @EncodeDefault
    val suggestionsLimit: Int = 100,
)