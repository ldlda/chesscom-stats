package com.ldlda.chesscom_stats.store

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import java.util.Date

@Fts4
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") val id: Int = 0,
    @ColumnInfo(name = "player_id") val playerId: Long?, // fuck some places dont
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo("favorite_since") val favoriteSince: Date = Date(System.currentTimeMillis()),
)
