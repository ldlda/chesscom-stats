package com.ldlda.chesscom_stats.store

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users", indices = [Index("player_id", "username", unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "player_id") val playerId: Long, // fuck some places dont
    val username: String,
    @ColumnInfo("favorite_since") val favoriteSince: Date = Date(System.currentTimeMillis()),
)
