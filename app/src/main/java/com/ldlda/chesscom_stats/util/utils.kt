package com.ldlda.chesscom_stats.util

import okhttp3.Cache
import okhttp3.HttpUrl
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
