package com.ldlda.chesscom_stats.utils.cache

import java.util.concurrent.ConcurrentHashMap

/**
 * Super-simple in-memory TTL cache.
 * Thread-safe for concurrent reads/writes (ConcurrentHashMap).
 */
class TimedCache<T>(private val ttlMillis: Long) {
    private data class Entry<T>(val value: T, val expiresAt: Long)

    private val map = ConcurrentHashMap<String, Entry<T>>()

    fun get(key: String): T? {
        val e = map[key] ?: return null
        val now = System.currentTimeMillis()
        return if (now < e.expiresAt) e.value else {
            // lazy eviction
            map.remove(key, e)
            null
        }
    }

    fun put(key: String, value: T) {
        val expires = System.currentTimeMillis() + ttlMillis
        map[key] = Entry(value, expires)
    }

    fun clear() = map.clear()
}