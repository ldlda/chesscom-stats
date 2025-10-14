@file:UseSerializers(InstantEpochSecondSerializer::class)

package com.ldlda.chesscom_stats.api.data.player.stats

import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class RatingDate(
    override val rating: Int,
    override val date: Instant,
) : BaseRatingDate() {
    companion object {
        fun Stats.LastRecord.toRatingDate() = RatingDate(rating, date)
        fun Stats.BestRecord.toRatingDate() = RatingDate(rating, date)
    }
}