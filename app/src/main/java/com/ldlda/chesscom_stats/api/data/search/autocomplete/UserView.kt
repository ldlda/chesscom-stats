@file:UseSerializers(InstantParseSerializer::class, HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.search.autocomplete

import com.ldlda.chesscom_stats.api.data.player.Title
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.InstantParseSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import okhttp3.HttpUrl
import java.time.Instant

@Serializable
data class UserView(
    val userId: Long,
    val username: String,

//    @Serializable(TheHttpUrlSerializer::class)
    @SerialName("avatar")
    private val avatarStruct: AvatarSurrogate,

    /**
    finally a use for [this fuckass][com.ldlda.chesscom_stats.api.fetch.ChessApiService.country]

    update: still no use for that fuckass
     */
    val country: String,
    val createdAt: Instant,

    val firstName: String? = null,
    val lastName: String? = null,

    val chessTitle: Title? = null,

    // one second after creation but i think im not certain
    // last profile update, above line is what if bro just joined
    val updatedAt: Instant? = null,

    ) {

    val avatar get() = avatarStruct.url
    val ecoAvatar get() = avatarStruct.badUrl

    @Serializable
    class AvatarSurrogate(
        @SerialName("highResolutionUrl")
        val url: HttpUrl,
        @SerialName("url")
        val badUrl: HttpUrl,
    )

    // i do need eco sometimes
    class TheHttpUrlSerializer : KSerializer<HttpUrl> {
        override val descriptor = AvatarSurrogate.serializer().descriptor
        override fun deserialize(decoder: Decoder): HttpUrl =
            decoder.decodeSerializableValue(AvatarSurrogate.serializer()).url


        override fun serialize(
            encoder: Encoder,
            value: HttpUrl
        ) {
            // random ahh:
            encoder.encodeSerializableValue(
                AvatarSurrogate.serializer(),
                AvatarSurrogate(value, value)
            )
        }
    }
}