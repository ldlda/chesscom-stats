package com.ldlda.chesscom_stats.util

import com.ldlda.chesscom_stats.api.fetch.ChessApiClient.Companion.CHESS_API_URL
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.File

fun buildCache(parent: File, dir: String, maxSize: Long) = Cache(
    File(parent, dir), maxSize
)

val malformedUrl = { "Malformed URL" }
val invalidUrlBase = { "Invalid URL base" }


fun check(base: HttpUrl, target: HttpUrl) =
    target.toString().startsWith(target.toString()) // thats easy
// or you can check host, port, and encodedpath to string for if ever we have anything about users and passwords passed in there

/**
 * @param fen valid fen (no need prior encoding)
 * @param size enum: 0 (default) 1 2 3
 */
fun buildIconLink(fen: String, size: Int? = null) =
    HttpUrl.Builder().scheme("https").host("www.chess.com")
        .addPathSegment("dynboard")
        .addQueryParameter("fen", fen)
        .apply { size?.let { this.addQueryParameter("size", size.toString()) } }
        .build()

fun buildChessComMemberLink(username: String) =
    HttpUrl.Builder().scheme("https").host("www.chess.com")
        .addPathSegment("member")
        .addPathSegment(username).build()

// this iae on not //api.chess.com/pub/country
@Throws(IllegalArgumentException::class)
fun getCountryApiUrl(fuckass: HttpUrl, base: HttpUrl = CHESS_API_URL.toHttpUrl()): String {
    require(
        fuckass.host == base.host && base.port == fuckass.port && fuckass.encodedPath.startsWith(
            base.encodedPath
        )
    ) { "bad base" }
    require(fuckass.encodedPathSegments.getOrNull(base.pathSize + 1) == "country") { "bad url" }
    return requireNotNull(fuckass.encodedPathSegments.getOrNull(base.pathSize + 2)) { "bad url" }
}
