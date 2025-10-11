package com.ldlda.chesscom_stats.util.serialize.tostring

import kotlinx.serialization.SerializationException
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

object HttpUrlSerializer : ToStringSerializer<HttpUrl>("HttpUrl") {
    override fun fromString(string: String): HttpUrl {
        return try {
            string.toHttpUrl()
        } catch (e: IllegalArgumentException) {
            throw SerializationException("Invalid URI string: '$string'", e)
        }
    }
}