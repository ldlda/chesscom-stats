@file:UseSerializers(URISerializer::class)

package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.utils.serialize.URISerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URI

// Lightweight view with the fields leaderboards actually provides
fun Player.toPlayerPreview() = PlayerPreview(
    playerId = playerId,
    username = username,
    profilePage = profilePage,
    countryUrl = countryUrl,
    status = status,
    title = title,
    name = name,
    avatarUrl = avatarUrl
)

fun LeaderboardEntry.toPlayerPreview() = PlayerPreview(
    playerId = playerId,
    username = username,
    profilePage = profilePage,
    countryUrl = countryUrl,
    status = status,
    title = title,
    name = name,
    avatarUrl = avatarUrl
)

// A minimal “preview” that mirrors what leaderboards returns about a player.
@Serializable
data class PlayerPreview(
    @SerialName("player_id") val playerId: Long,
    val username: String,
    @SerialName("url") val profilePage: URI? = null,
    @SerialName("country") val countryUrl: URI? = null,
    val status: String? = null,
    val title: String? = null,
    val name: String? = null,
    @SerialName("avatar") val avatarUrl: URI? = null,
)