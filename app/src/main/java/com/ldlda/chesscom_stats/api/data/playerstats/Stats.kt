@file:UseSerializers(InstantEpochSecondSerializer::class, URISerializer::class)

package com.ldlda.chesscom_stats.api.data.playerstats

import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.URISerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URI
import java.time.Instant

@Serializable
data class Stats<T>(
    val last: LastRecord,
    val best: BestRecord? = null,
    val record: T,
) {
    @Serializable
    data class LastRecord(val rating: Int, val date: Instant, val rd: Int)

    @Serializable
    data class BestRecord(val rating: Int, val date: Instant, val game: URI)
}