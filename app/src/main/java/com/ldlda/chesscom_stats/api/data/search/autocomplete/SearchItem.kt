package com.ldlda.chesscom_stats.api.data.search.autocomplete

import android.os.Parcelable
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.util.parcelize.httpurl.HttpUrlParceler
import com.ldlda.chesscom_stats.util.serialize.tostring.InstantParseSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import okhttp3.HttpUrl
import java.time.Instant

@Parcelize
@TypeParceler<HttpUrl, HttpUrlParceler>()
@Serializable
data class SearchItem(
    val userView: UserView,
    val country: CountryInfo, // + .id which idc
    @Serializable(with = InstantParseSerializer::class)
    val lastLoginDate: Instant,
    val bestRating: Int? = null,
    @SerialName("presence")
    @Serializable(FuckAssSerializer::class)
    val online: Boolean,
    val ratings: List<Rating> = emptyList()
) : Parcelable {
    internal class FuckAssSerializer : KSerializer<Boolean> {
        override val descriptor: SerialDescriptor
            get() = String.serializer().descriptor

        override fun serialize(
            encoder: Encoder,
            value: Boolean
        ) {
            encoder.encodeString("FRIENDS_SEARCH_PRESENCE_STATUS_${if (value) "ONLINE" else "OFFLINE"}")
        }

        override fun deserialize(decoder: Decoder): Boolean {
            return "ONLINE" in decoder.decodeString()
        }
    }
}