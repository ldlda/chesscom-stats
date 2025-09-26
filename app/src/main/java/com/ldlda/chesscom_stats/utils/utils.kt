package com.ldlda.chesscom_stats.utils

import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.api.fetch.DefaultChessApi
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryJava
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache
import okhttp3.Cache
import java.io.File

fun defaultWithCache(cache: Cache): ChessRepositoryJava = ChessRepositoryJava(
    ChessRepositoryTimedCache(
        ChessApiClient(
            okHttp = DefaultChessApi.okHttp.newBuilder().cache(cache).build()
        )
    )
)

fun buildCache(parent: File, dir: String, maxSize: Long): Cache = Cache(
    File(parent, dir), maxSize
)