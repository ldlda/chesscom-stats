package com.ldlda.chesscom_stats.api.data.country

import android.os.Parcelable
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.util.getCountryApiUrl
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Parcelize
@Serializable
data class CountryInfo(
    val name: String,
    val code: String // Locale.IsoCountryCode + random shit
) : Parcelable {
    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true }

        @JvmStatic
        fun fromJSON(jsonString: String) =
            jsonFormat.decodeFromString(serializer(), jsonString)

        /**
         * Now just throws
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun extractCountryCodeFromUrl(
            base: String = ChessApiClient.Companion.CHESS_API_URL,
            countryUrl: String,
        ): String {
            val a = requireNotNull(countryUrl.toHttpUrlOrNull()) { "bad url" }
            val b = requireNotNull(base.toHttpUrlOrNull()) { "bad base" }
            return getCountryApiUrl(a, b)
        }
        /*
         cs.strip_prefix(bs)
         .filter(|this| this.len() == 2 || this.first().map_or(false, |first| first == "country"))
         .and_then(|list| list.last())
         .filter(|second| second.is_empty())  // lowk this isnt better
         */

        /*
         * this requires the input to be chesscom
         */
    }

    fun toJSON() = jsonFormat.encodeToString(this)
}