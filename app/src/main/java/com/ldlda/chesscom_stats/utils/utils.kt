package com.ldlda.chesscom_stats.utils

import okhttp3.Cache
import java.io.File

fun buildCache(parent: File, dir: String, maxSize: Long) = Cache(
    File(parent, dir), maxSize
)