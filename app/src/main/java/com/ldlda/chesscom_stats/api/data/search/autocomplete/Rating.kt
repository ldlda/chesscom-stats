package com.ldlda.chesscom_stats.api.data.search.autocomplete

import com.ldlda.chesscom_stats.api.data.search.autocomplete.UserView.URISurrogate
import com.ldlda.chesscom_stats.api.data.timeclass.Class
import com.ldlda.chesscom_stats.api.data.timeclass.Time
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Rating(
    val variantTime: Time,
    val variantClass: Class,
    val rating: Int,
) {
    @Serializable
    class RatingSurrogate(
        val variantTimeclass: String,
        val rating: Int
    )

    class RatingSerializer : KSerializer<Rating> {
        override val descriptor = URISurrogate.serializer().descriptor
        override fun deserialize(decoder: Decoder): Rating {
            val surrogate = decoder.decodeSerializableValue(RatingSurrogate.serializer())
            val regex = Regex("^FRIENDS_SEARCH_VARIANT_TIMECLASS_(\\w+)_(\\w+)$")
            val maybe = regex.matchEntire(surrogate.variantTimeclass)
                ?: throw SerializationException("oh noes. it doesnt match! i got $surrogate")
            val (classStr, timeStr) = maybe.destructured
            val (classType, time) = runCatching {
                Class.goodValueOf(classStr) to Time.goodValueOf(timeStr)
            }.getOrElse { exception ->
                throw SerializationException(
                    "can not recognize type",
                    exception
                )
            }
            return Rating(
                time,
                classType,
                surrogate.rating
            )
        }

        override fun serialize(
            encoder: Encoder,
            value: Rating
        ) {
            // random ahh:
            throw NotImplementedError("no")
        }
    }
}
