package com.ldlda.chesscom_stats.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Upsert

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg users: User)

    @Upsert
    fun upsertUsers(vararg users: User)

    @androidx.room.Delete
    fun deleteUsers(vararg users: User)

    @androidx.room.Query("SELECT *, `rowid` FROM users")
    fun queryAllUsers(): List<User>
}
