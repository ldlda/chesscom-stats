@file:UseSerializers(InstantEpochSecondSerializer::class, URISerializer::class)

package com.ldlda.chesscom_stats.api.data.player.stats

import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.URISerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URI
import java.time.Instant

// naming hell
@Serializable
data class Stats<T : BaseRecord>(
    val last: LastRecord,
    val best: BestRecord? = null,
    val record: T,
) {
    @Serializable
    data class LastRecord(override val rating: Int, override val date: Instant, val rd: Int) :
        BaseRatingDate()

    @Serializable
    data class BestRecord(override val rating: Int, override val date: Instant, val game: URI) :
        BaseRatingDate()
}