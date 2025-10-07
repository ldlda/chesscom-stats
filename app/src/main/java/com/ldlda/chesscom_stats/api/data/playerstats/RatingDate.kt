@file:UseSerializers(InstantEpochSecondSerializer::class)

package com.ldlda.chesscom_stats.api.data.playerstats

import com.ldlda.chesscom_stats.utils.serialize.InstantEpochSecondSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class RatingDate(
    val rating: Int,
    val date: Instant,
) {
    fun Stats.LastRecord.toRatingDate() = RatingDate(rating, date)
    fun Stats.BestRecord.toRatingDate() = RatingDate(rating, date)
}