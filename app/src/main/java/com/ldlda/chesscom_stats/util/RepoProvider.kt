package com.ldlda.chesscom_stats.util

import android.content.Context
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache
import okhttp3.Cache

object RepoProvider {
    fun defaultRepository(context: Context): ChessRepository {
        val cache: Cache = buildCache(context.cacheDir, "http-cache", 20L shl 20)
        val clientWithCache = ChessApiClient(
            okHttp = ChessApiClient.Companion.defaultOkHttp.newBuilder().cache(cache).build()
        )
        return ChessRepositoryTimedCache(clientWithCache)
    }
}