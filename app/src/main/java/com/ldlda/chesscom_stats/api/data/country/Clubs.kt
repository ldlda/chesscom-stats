@file:UseSerializers(HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.country

import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl

@Serializable
data class Clubs(@SerialName("clubs") val clubURLs: List<HttpUrl> = emptyList())
