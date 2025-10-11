package com.ldlda.chesscom_stats.api.data.player

import android.support.annotation.StringRes
import com.ldlda.chesscom_stats.R
import kotlinx.serialization.Serializable

@Serializable
enum class Title(
    @get:JvmName("displayName") val displayName: String,
    @param:StringRes val stringRes: Int?
) {
    GM("Grandmaster", R.string.grandmaster),
    WGM("Woman Grandmaster", R.string.grandmaster),
    IM("International Master", R.string.i_master),
    WIM("Woman International Master", R.string.i_master),
    FM("FIDE Master", R.string.fide_master),
    WFM("Woman FIDE Master", R.string.fide_master),
    NM("National Master", null),
    WNM("Woman National Master", null),
    CM("Candidate Master", R.string.fide_cand_master),
    WCM("Woman Candidate Master", R.string.fide_cand_master),
    ;

    @get:JvmName("stringId")
    val stringId get() = stringRes
}