package com.ldlda.chesscom_stats.utils

import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache
import okhttp3.Cache
import java.io.File


fun defaultWithCache(cache: Cache): ChessRepoAdapterJava = ChessRepoAdapterJava(
    ChessRepositoryTimedCache(
        ChessApiClient(
            okHttp = ChessApiClient().okHttp.newBuilder().cache(cache).build()
        )
    )
)


fun buildCache(parent: File, dir: String, maxSize: Long) = Cache(
    File(parent, dir), maxSize
)