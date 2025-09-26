package com.ldlda.chesscom_stats.utils.cache

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

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

/**
 * Captures ETag and response body on 200; if server returns 304 and we have a cached body,
 * synthesize a 200 OK with the cached body so Retrofit converters still work.
 */
class CaptureEtagAndServe304FromCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val key = EtagCache.key(request.url)

        val response = chain.proceed(request)

        return when (response.code) {
            200 -> {
                val bodyBytes = response.body.byteString()
                val etag = response.header("ETag")
                if (bodyBytes.size > 0) {
                    EtagCache.put(key, etag, bodyBytes)
                } else {
                    // No body? Drop any stale cache
                    EtagCache.remove(key)
                }
                // We consumed the body; rebuild response with new body
                response.newBuilder()
                    .body(bodyBytes.toResponseBody(response.body.contentType()))
                    .build()
            }
            304 -> {
                val cached = EtagCache.getBody(key)
                if (cached != null) {
                    // Synthesize a 200 OK with cached body so converters can arse as usual
                    Response.Builder()
                        .request(request)
                        .protocol(response.protocol)
                        .code(200)
                        .message("OK (from cache)")
                        .body(cached.toResponseBody("application/json".toMediaType()))
                        .headers(response.headers)
                        .build()
                } else {
                    response
                }
            }
            else -> response
        }
    }
}
