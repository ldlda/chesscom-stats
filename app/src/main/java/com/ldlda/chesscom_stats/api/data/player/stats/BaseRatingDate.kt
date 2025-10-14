@file:UseSerializers(InstantEpochSecondSerializer::class)

package com.ldlda.chesscom_stats.api.data.player.stats

import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import kotlinx.serialization.UseSerializers
import java.time.Instant


abstract class BaseRatingDate {
    abstract val rating: Int
    abstract val date: Instant
}