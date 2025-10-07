package com.ldlda.chesscom_stats.utils

import okhttp3.Cache
import okhttp3.HttpUrl
import java.io.File

fun buildCache(parent: File, dir: String, maxSize: Long) = Cache(
    File(parent, dir), maxSize
)

val malformedUrl = { "Malformed URL" }
val invalidUrlBase = { "Invalid URL base" }


fun Boolean.checkBaseAndTarget(base: HttpUrl, target: HttpUrl) =
    ldaCheckThis<Unit, Unit>(check = this, strict = true) {
        require(base.host == target.host && base.port == target.port, invalidUrlBase)
        require(base.encodedPathSegments.size <= target.encodedPathSegments.size, invalidUrlBase)
        requireNot(base.encodedPathSegments.last().isBlank(), invalidUrlBase)
    }
