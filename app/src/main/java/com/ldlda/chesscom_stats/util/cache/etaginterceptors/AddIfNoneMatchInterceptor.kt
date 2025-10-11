package com.ldlda.chesscom_stats.util.cache.etaginterceptors

import com.ldlda.chesscom_stats.util.cache.EtagCache
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds If-None-Match for known cacheable endpoints if we have an ETag stored.
 */
class AddIfNoneMatchInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val key = EtagCache.key(req.url)
        val etag = EtagCache.getEtag(key)
        val newReq = if (!etag.isNullOrEmpty()) {
            req.newBuilder().header("If-None-Match", etag).build()
        } else req
        return chain.proceed(newReq)
    }
}