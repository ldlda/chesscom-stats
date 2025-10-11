@file:UseSerializers(HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.player.games.monthly

import com.ldlda.chesscom_stats.util.checkFn
import com.ldlda.chesscom_stats.util.invalidUrlBase
import com.ldlda.chesscom_stats.util.malformedUrl
import com.ldlda.chesscom_stats.util.requiredNotNullOr
import com.ldlda.chesscom_stats.util.requiredNotOr
import com.ldlda.chesscom_stats.util.requiredOr
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl

@Serializable
data class MonthlyArchives(val archives: List<HttpUrl> = emptyList()) {
    data class Detail(val username: String, val year: String, val month: String)


    fun archivesDetail(baseUrl: HttpUrl, check: Boolean = false): List<Detail> {
        return archives.mapNotNull { el ->
            el.runCatching {
                this.mapMonthlyArchivesDetail(
                    baseUrl, check = check
                )
            } // free throw
                .getOrNull()
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
            val checkFn = checkFn(true)
            val bs = base.encodedPathSegments.let {
                checkFn { it.last().isBlank() requiredOr invalidUrlBase }
                it.subList(0, it.size - 1)
            }
            val ts = target.encodedPathSegments
            checkFn {
                (base.host == target.host && base.port == target.port) requiredOr invalidUrlBase
                // "player" {username} "games" yyyy MM vs /
                (ts.size < bs.size + 5 || (ts.size > bs.size + 5 && ts.subList(bs.size + 5, ts.size)
                    .any { !it.isBlank() })) requiredNotOr malformedUrl
                // ////
                // username must not be spaces, nothing after this is fine (redir) but there are two keys (players & clubs)
                // malformed in url
                (ts.subList(0, bs.size) == bs) requiredOr invalidUrlBase
                (ts.getOrNull(bs.size) == "player" && ts.getOrNull(bs.size + 2) == "games") requiredOr malformedUrl
            }
            val username = ts.getOrNull(bs.size + 1) requiredNotNullOr malformedUrl
            val year = ts.getOrNull(bs.size + 3) requiredNotNullOr malformedUrl
            val month = ts.getOrNull(bs.size + 4) requiredNotNullOr malformedUrl
            checkFn {
                username.takeIf { it.isNotBlank() } requiredNotNullOr malformedUrl

                year.takeIf { year -> year.length == 4 && year.all { it.isDigit() } }
                    .requiredNotNullOr(malformedUrl)

                month.takeIf { month -> month.length == 2 && month.all { it.isDigit() } }
                    .requiredNotNullOr(malformedUrl)

            }
            return Detail(username, year, month)
        }
    }
}