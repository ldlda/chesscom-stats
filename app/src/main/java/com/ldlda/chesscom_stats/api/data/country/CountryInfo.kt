package com.ldlda.chesscom_stats.api.data.country

import android.os.Parcelable
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.util.checkFn
import com.ldlda.chesscom_stats.util.invalidUrlBase
import com.ldlda.chesscom_stats.util.malformedUrl
import com.ldlda.chesscom_stats.util.requiredNotNullOr
import com.ldlda.chesscom_stats.util.requiredOr
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

        /*
         * lowk the stupidest thing ever
         */
        /**
         * @param check lowest friction if false still going to throw tho
         */
        @Deprecated("NEVER use this function")
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun extractCountryCodeFromUrl(
            base: String = ChessApiClient.Companion.CHESS_API_URL,
            countryUrl: String,
            check: Boolean = false
        ): String {

            val base = base.toHttpUrlOrNull() requiredNotNullOr malformedUrl
            val countryUrl = countryUrl.toHttpUrlOrNull() requiredNotNullOr malformedUrl
//            val checkFn: (() -> Unit) -> Unit = { if (check) it() }
            val checkFn = checkFn(check)
            val cs = countryUrl.encodedPathSegments
            val bs = base.encodedPathSegments.let {
                // remove the path after the last / which is blank
                checkFn { it.last().isBlank() requiredOr invalidUrlBase }
                it.subList(0, it.size - 1)
            }
            checkFn {
                (base.host == countryUrl.host
                        && base.port == countryUrl.port
                        && cs.subList(0, bs.size) == bs) requiredOr invalidUrlBase
                (cs.size >= bs.size + 2 &&
                        cs.getOrNull(bs.size) == "country") requiredOr malformedUrl
                // country and code (and maybe players)
            }
            val ret = cs.getOrNull(bs.size + 1)
            checkFn {
                ret?.takeIf { it.isNotBlank() } requiredNotNullOr
                        malformedUrl
            }
            return ret requiredNotNullOr malformedUrl
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