@file:UseSerializers(HttpUrlSerializer::class, InstantEpochSecondSerializer::class)

package com.ldlda.chesscom_stats.api.data.puzzle

import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl
import java.time.Instant

@Serializable
data class Puzzle(
    val title: String,
    val url: HttpUrl,
    val fen: String,
    val pgn: String,
    val image: HttpUrl,
    @SerialName("publish_time")
    val publishTime: Instant,
)
