@file:UseSerializers(HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.puzzle

import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl

@Serializable
data class Puzzle(
    val title: String,
    val url: HttpUrl,
    val fen: String,
    val pgn: String,
    val image: HttpUrl
)
