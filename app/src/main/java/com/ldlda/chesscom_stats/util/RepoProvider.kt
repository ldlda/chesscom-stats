package com.ldlda.chesscom_stats.util

import android.content.Context
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient.Companion.ldaBuildRetrofit
import com.ldlda.chesscom_stats.api.fetch.ChessApiService
import com.ldlda.chesscom_stats.api.fetch.PrivateApiService
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache
import okhttp3.Cache
import retrofit2.create

object RepoProvider {
    @JvmStatic
    @JvmOverloads
    fun defaultRepository(
        context: Context,
        cache: Cache = buildCache(
            context.cacheDir,
            "http-cache",
            20L shl 20
        ) // please kotlin do your magic
    ): ChessRepositoryTimedCache {
        val r = ChessApiClient.defaultOkHttp.newBuilder().cache(cache).build()
            .ldaBuildRetrofit()
        val publicService: ChessApiService = r.create()
        val privateApiService: PrivateApiService = r.create()
        return ChessRepositoryTimedCache(publicService, privateApiService)
    }

    // default that is appcontexted. i dont like appcontexted because they get ahh.
    @get:JvmStatic
    lateinit var default: ChessRepositoryTimedCache
        private set

    @JvmStatic
    fun setupAppContext(context: Context): ChessRepositoryTimedCache {
        default = defaultRepository(context)
        return default
    }
}