@file:UseSerializers(HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.country

import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class Players(@SerialName("players") val playerUsernames: List<String> = emptyList())
