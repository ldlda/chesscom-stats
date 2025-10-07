@file:UseSerializers(URISerializer::class)

package com.ldlda.chesscom_stats.api.data.playergames

import com.ldlda.chesscom_stats.utils.invalidUrlBase
import com.ldlda.chesscom_stats.utils.ldaCheckThis
import com.ldlda.chesscom_stats.utils.malformedUrl
import com.ldlda.chesscom_stats.utils.requireNot
import com.ldlda.chesscom_stats.utils.serialize.tostring.URISerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.net.URI

@Serializable
data class MonthlyArchives(val archives: List<URI> = emptyList()) {
    fun iterator(): MonthlyArchivesIterator = MonthlyArchivesIterator(this)

    data class Detail(val username: String, val year: String, val month: String)


    fun archivesDetail(baseUrl: HttpUrl, check: Boolean = false): List<Detail> {
//        val chesscom =
        return archives.mapNotNull { el ->
            el.toHttpUrlOrNull()
                ?.runCatching {
                    this.mapMonthlyArchivesDetail(
                        baseUrl,
                        check = check
                    )
                } // free throw
                ?.getOrNull()
        }
    }

    companion object {
        /**
         * Unsafe Function; can break
         *
         * @param check lowest friction if false; still going to throw
         */
        @Deprecated("do NOT use this bs")
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun HttpUrl.mapMonthlyArchivesDetail(baseUrl: HttpUrl, check: Boolean = false): Detail {
            // commented out code is some validation shit. ima priv this so i dont have to check
            val base = baseUrl
            val target = this
            val bs = base.encodedPathSegments.let {
                ldaCheckThis<Unit, Unit>(check, strict = true) {
                    require(
                        it.last().isBlank(),
                        invalidUrlBase
                    )
                }
                it.subList(0, it.size - 1)
            }
            val ts = target.encodedPathSegments
            ldaCheckThis<Unit, Unit>(check, strict = true) {
                require(base.host == target.host && base.port == target.port, invalidUrlBase)
                requireNot(
                    ts.size < bs.size + 5,
                    malformedUrl
                ) // "player" {username} "games" yyyy MM vs /
                requireNot(
                    ts.size > bs.size + 5 // username must not be spaces, nothing after this is fine
                            && ts.subList(bs.size + 5, ts.size).any { !it.isBlank() },
                    malformedUrl
                ) // malformed in url
                require(ts.subList(0, bs.size) == bs, invalidUrlBase)
                require(ts.getOrNull(bs.size) == "player", malformedUrl)
                require(ts.getOrNull(bs.size + 2) == "games", malformedUrl)
            }
            val username = requireNotNull(ts.getOrNull(bs.size + 1), malformedUrl)
            val year = requireNotNull(ts.getOrNull(bs.size + 3), malformedUrl)
            val month = requireNotNull(ts.getOrNull(bs.size + 4), malformedUrl)
            ldaCheckThis<Unit, Unit>(check, strict = true) {
                requireNotNull(username.takeIf { it.isNotBlank() }, malformedUrl)
                requireNotNull(
                    year.takeIf { year -> year.length == 4 && year.all { it.isDigit() } },
                    malformedUrl
                )
                requireNotNull(
                    month.takeIf { month -> month.length == 2 && month.all { it.isDigit() } },
                    malformedUrl
                )
            }
            return Detail(username, year, month)
        }
    }
}