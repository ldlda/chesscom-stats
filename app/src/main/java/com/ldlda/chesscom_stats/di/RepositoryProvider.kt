package com.ldlda.chesscom_stats.di

import android.content.Context
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache
import com.ldlda.chesscom_stats.utils.buildCache
import okhttp3.Cache

object RepositoryProvider {
    @JvmStatic
    fun defaultRepository(context: Context): ChessRepository {
        val cache: Cache = buildCache(context.cacheDir, "http-cache", 20L shl 20)
        val base = ChessApiClient()
        val clientWithCache = ChessApiClient(
            baseUrl = base.baseUrl,
            okHttp = base.okHttp.newBuilder().cache(cache).build()
        )
        return ChessRepositoryTimedCache(clientWithCache)
    }
}
