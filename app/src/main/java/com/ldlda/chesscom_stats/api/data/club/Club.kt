@file:UseSerializers(HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.club

import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl

@Serializable
data class Club(
    @SerialName("@id") val id: HttpUrl,
    val name: String?,

    @SerialName("club_id") val clubId: Long,

    val icon: String?,

    val country: HttpUrl?,
    @SerialName("average_daily_rating") val averageDailyRating: Int,
    @SerialName("members_count") val membersCount: Int,

    val created: Long,
    @SerialName("last_activity") val lastActivity: Long,
    val visibility: String?,
    @SerialName("join_request") val joinRequest: HttpUrl?,  // URL to join page

    val admin: List<HttpUrl>?,  // list of player profile URLs
    val description: String? // plain text or HTML description
)
