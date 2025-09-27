package com.ldlda.chesscom_stats.utils.cache

import java.util.concurrent.ConcurrentHashMap

/**
 * Super-simple in-memory TTL cache.
 * Thread-safe for concurrent reads/writes (ConcurrentHashMap).
 */
class TimedCache<T>(private val ttlMillis: Long) {
    private val map = ConcurrentHashMap<String, Entry<T>>()

    fun get(key: String): T? {
        val e = map[key] ?: return null
        val now = System.currentTimeMillis()
        return if (now - e.time <= ttlMillis) e.value else {
            // lazy eviction
            map.remove(key, e)
            null
        }
    }

    fun put(key: String, value: T) {
        map[key] = Entry(value, System.currentTimeMillis())
    }

    fun clear() = map.clear()

    data class Entry<T>(val value: T, val time: Long)
}
