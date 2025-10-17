@file:UseSerializers(HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.api.data.leaderboards.LeaderboardEntry
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.Title
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl


/* demo
fun ChessSearchItem.toPlayerPreview() = PlayerPreview(
    playerId = userView.userId,
    username = userView.username,
    profilePage = URI("https://www.chess.com/member/${userView.username}"),
    title = userView.title,
    avatarUrl = userView.avatar
)
*/
// A minimal “preview” that mirrors what leaderboards returns about a player.
@Serializable
data class PlayerPreview(
    @SerialName("player_id") val playerId: Long,
    val username: String,
    @SerialName("url") val profilePage: HttpUrl? = null,
    @SerialName("country") val countryUrl: HttpUrl? = null,
    val status: String? = null,
    val title: Title? = null,
    val name: String? = null,
    @SerialName("avatar") val avatarUrl: HttpUrl? = null,
) {
    // Lightweight view with the fields leaderboards actually provides
    fun Player.toPlayerPreview() = PlayerPreview(
        playerId = playerId,
        username = username,
        profilePage = profilePage,
        countryUrl = countryUrl,
        status = status.name,
        title = title,
        name = name,
        avatarUrl = avatarUrl
    )

    fun LeaderboardEntry.toPlayerPreview() = PlayerPreview(
        playerId = playerId,
        username = username,
        profilePage = profilePage,
        countryUrl = countryApiUrl,
        status = status?.name,
        title = title,
        name = name,
        avatarUrl = avatarUrl
    )
}