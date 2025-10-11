@file:UseSerializers(HttpUrlSerializer::class, InstantEpochSecondSerializer::class)

package com.ldlda.chesscom_stats.api.data.player.clubs

import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl
import java.time.Instant

@Serializable
data class PlayerClub(
    @SerialName("@id") val clubApiUrl: HttpUrl, // URL of Club endpoint
    val name: String, // Club's name
    @SerialName("last_activity") val lastActivity: Instant, //timestamp of last activity
    val icon: HttpUrl, // Club's icon url
    val url: HttpUrl, // Club's url
    val joined: Instant  // Timestamp of when player joined the Club
)
