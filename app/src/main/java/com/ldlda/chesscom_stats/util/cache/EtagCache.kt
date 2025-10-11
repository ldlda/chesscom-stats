package com.ldlda.chesscom_stats.util.cache

import okhttp3.HttpUrl
import okio.ByteString
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory ETag + raw body cache keyed by full URL path + query.
 * Small and process-bound; replace with disk if needed later.
 */
object EtagCache {
    private data class Entry(val etag: String?, val body: ByteString)

    private val map = ConcurrentHashMap<String, Entry>()

    fun key(url: HttpUrl): String {
        val path = url.encodedPath
        val query = url.encodedQuery
        return if (query.isNullOrEmpty()) path else "$path?$query"
    }

    fun getEtag(key: String): String? = map[key]?.etag

    fun getBody(key: String): ByteString? = map[key]?.body

    fun put(key: String, etag: String?, body: ByteString) {
        // null etag is crazy
        map[key] = Entry(etag, body)
    }

    fun remove(key: String) {
        map.remove(key)
    }

    fun clear() = map.clear()
}