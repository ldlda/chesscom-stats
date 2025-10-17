package com.ldlda.chesscom_stats.ui.lessons.data

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
class Lesson(
    @JvmField val dataTitle: String,
    @JvmField @param:StringRes val dataDesc: Int,
    @JvmField val dataLevel: String,
    @JvmField @param:DrawableRes val dataImage: Int,
    @JvmField @param:ColorInt val color: Int
) : Parcelable
