package com.ldlda.chesscom_stats.store

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface FavoriteDao {
    // === INSERT/UPSERT ===
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg users: User)

    @Upsert
    suspend fun upsert(vararg users: User)

    // === DELETE ===
    @Delete
    suspend fun delete(vararg users: User): Int // Returns number of deleted rows

    @Query("DELETE FROM users WHERE player_id = :playerId")
    suspend fun deleteByPlayerId(playerId: Long): Int

    @Query("DELETE FROM users WHERE username = :username")
    suspend fun deleteByUsername(username: String): Int

    // === QUERY ===
    @Query("SELECT * FROM users ORDER BY favorite_since DESC")
    fun getAllUsers(): LiveData<List<User>> // Reactive - auto-updates UI

    @Query("SELECT * FROM users ORDER BY favorite_since DESC")
    suspend fun getAllUsersOnce(): List<User> // One-shot query

    @Query("SELECT * FROM users WHERE player_id = :playerId")
    suspend fun getUserByPlayerId(playerId: Long): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    // === EXISTS (efficient, doesn't fetch whole User) ===
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE player_id = :playerId)")
    suspend fun existsByPlayerId(playerId: Long): Boolean

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getCount(): Int
}
