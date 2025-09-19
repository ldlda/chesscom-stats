package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.utils.parse.URISerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI

/**
should be https://api.chess.com/pub/player/{ldabsbplef}
 */
@Serializable
data class Player(
    val username: String,
    @SerialName("url")
    @Serializable(with = URISerializer::class)
    val profilePage: URI,

    @SerialName("player_id")
    val playerId: Long,

    @SerialName("avatar")
    @Serializable(with = URISerializer::class)
    val profilePictureResource: URI? = null,


    ) {
    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true }

        @JvmStatic
        fun fromJson(jsonString: String): Player = jsonFormat.decodeFromString(Player.serializer(), jsonString)
    }
}
