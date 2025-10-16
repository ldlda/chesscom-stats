package com.ldlda.chesscom_stats.store

import android.content.Context
import androidx.room.Room

object GlobalDB {
    @JvmStatic
    lateinit var db: FavoriteDatabase
        private set

    /**
     * dont run ts again
     */
    @JvmStatic
    fun initDb(applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            FavoriteDatabase::class.java, "favorite-users"
        ).fallbackToDestructiveMigration(dropAllTables = false)
            .build().also { if (!::db.isInitialized) db = it }
}