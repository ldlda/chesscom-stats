package com.ldlda.chesscom_stats.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg users: User)

    @Upsert
    fun upsertUsers(vararg users: User)

    @androidx.room.Delete
    fun deleteUsers(vararg users: User)

    @Query("SELECT *, `rowid` FROM users")
    fun queryAllUsers(): List<User>

    @Query("Select *, `rowid` from users where player_id = :playerId")
    fun queryPlayerId(playerId: Long): List<User>

    @Query("Select *, `rowid` from users where username = :username")
    fun queryUsername(username: String): List<User>
}
