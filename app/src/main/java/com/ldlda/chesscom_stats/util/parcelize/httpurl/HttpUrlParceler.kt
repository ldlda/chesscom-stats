package com.ldlda.chesscom_stats.util.parcelize.httpurl

import android.os.Parcel
import kotlinx.parcelize.Parceler
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

/**
 * Custom Parceler for OkHttp's HttpUrl.
 * Allows HttpUrl to be passed in Intents/Bundles via Parcelable.
 * Handles both nullable and non-nullable HttpUrl fields.
 */
object HttpUrlParceler : Parceler<HttpUrl> {
    override fun create(parcel: Parcel): HttpUrl {
        val urlString = parcel.readString()
        return urlString?.toHttpUrl() ?: throw IllegalStateException("HttpUrl string is null")
    }

    override fun HttpUrl.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toString())
    }
}

/**
 * Nullable version of HttpUrlParceler for nullable HttpUrl? fields.
 * Use this with @WriteWith<HttpUrlParcelerNullable> for nullable fields.
 */
object HttpUrlParcelerNullable : Parceler<HttpUrl?> {
    override fun create(parcel: Parcel): HttpUrl? {
        val urlString = parcel.readString()
        return urlString?.toHttpUrl()
    }

    override fun HttpUrl?.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this?.toString())
    }
}