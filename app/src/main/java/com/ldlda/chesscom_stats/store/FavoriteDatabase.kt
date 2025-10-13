package com.ldlda.chesscom_stats.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    version = 2,
    entities = [User::class]
)
@TypeConverters(Converters::class)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}