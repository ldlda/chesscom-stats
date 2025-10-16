package com.ldlda.chesscom_stats.util

import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.File

fun buildCache(parent: File, dir: String, maxSize: Long) = Cache(
    File(parent, dir), maxSize
)

val malformedUrl = { "Malformed URL" }
val invalidUrlBase = { "Invalid URL base" }


fun Boolean.checkBaseAndTarget(base: HttpUrl, target: HttpUrl) =
    ldaCheckThis(check = this, strict = true) {
        (base.host == target.host && base.port == target.port) requiredOr invalidUrlBase
        (base.encodedPathSegments.size <= target.encodedPathSegments.size) requiredOr malformedUrl
        (base.encodedPathSegments.last().isBlank()) requiredOr invalidUrlBase
    }

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
fun getCountryApiUrl(fuckass: HttpUrl, base: HttpUrl = "//api.chess.com/pub/".toHttpUrl()): String {
    require(fuckass.host == base.host && fuckass.encodedPath.startsWith(base.encodedPath)) { "bad base" }
    require(fuckass.encodedPathSegments.getOrNull(1) == "country") { "bad url" }
    return requireNotNull(fuckass.encodedPathSegments.getOrNull(2)) { "bad url" }
}
