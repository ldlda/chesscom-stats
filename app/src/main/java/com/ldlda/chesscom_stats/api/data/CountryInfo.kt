package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.util.invalidUrlBase
import com.ldlda.chesscom_stats.util.ldaCheckThis
import com.ldlda.chesscom_stats.util.malformedUrl
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Serializable
data class CountryInfo(
    val name: String,
    val code: String // Locale.IsoCountryCode + random shit
) {
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
            base: String,
            countryUrl: String,
            check: Boolean = false
        ): String {
            val base = requireNotNull(base.toHttpUrlOrNull(), malformedUrl)
            val countryUrl = requireNotNull(countryUrl.toHttpUrlOrNull(), malformedUrl)
            val cs = countryUrl.encodedPathSegments
            val bs = base.encodedPathSegments.let {
                ldaCheckThis(check, strict = true) { require(it.last().isBlank(), invalidUrlBase) }
                it.subList(0, it.size - 1)
            }
            ldaCheckThis(check, strict = true) {
                require(
                    base.host == countryUrl.host && base.port == countryUrl.port,
                    malformedUrl
                )
                require(
                    cs.size >= bs.size + 2,
                    malformedUrl
                ) // country and code (and maybe players)
                require(cs.subList(0, bs.size) == bs, invalidUrlBase)
                require(cs.getOrNull(bs.size) == "country", malformedUrl)
            }
            val ret = cs.getOrNull(bs.size + 1)
            ldaCheckThis(check, strict = true) { requireNotNull(ret?.takeIf { it.isNotBlank() }, malformedUrl) }
            return requireNotNull(ret, malformedUrl)
        }
        /*
         cs.strip_prefix(bs)
         .filter(|this| this.len() == 2 || this.first().map_or(false, |first| first == "country"))
         .and_then(|list| list.last())
         .filter(|second| second.is_empty())  // lowk this isnt better
         */
    }

    fun toJSON() = jsonFormat.encodeToString(this)
}