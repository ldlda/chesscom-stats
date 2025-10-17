@file:UseSerializers(HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.player.games.monthly

import com.ldlda.chesscom_stats.api.fetch.ChessApiClient.Companion.CHESS_API_URL
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

@Serializable
data class MonthlyArchives(val archives: List<HttpUrl> = emptyList()) {
    data class Detail(val username: String, val year: String, val month: String)

    fun archivesDetail(baseUrl: HttpUrl = CHESS_API_URL.toHttpUrl()): List<Detail> {
        return archives.mapNotNull { el ->
            el.runCatching {
                // free throw
                this.mapMonthlyArchivesDetail(baseUrl)
            }.getOrNull()
        }
    }

    companion object {
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun HttpUrl.mapMonthlyArchivesDetail(baseUrl: HttpUrl = CHESS_API_URL.toHttpUrl()): Detail {
            val base = baseUrl
            val target = this
            require(base.host == target.host && base.port == target.port) { "bad url" }
            require(target.encodedPath.startsWith(base.encodedPath)) { "bad url" }
            require(
                target.encodedPathSegments.getOrNull(base.pathSize) == "player"
                        && target.encodedPathSegments.getOrNull(base.pathSize + 2) == "games"
            ) { "bad url" }
            val username =
                requireNotNull(target.encodedPathSegments.getOrNull(base.pathSize + 1)) { "bad url" }
            val year =
                requireNotNull(target.encodedPathSegments.getOrNull(base.pathSize + 3)) { "bad url" }
            val month =
                requireNotNull(target.encodedPathSegments.getOrNull(base.pathSize + 4)) { "bad url" }
            return Detail(username, year, month)
        }
    }
}