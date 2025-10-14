package com.ldlda.chesscom_stats.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ldlda.chesscom_stats.store.GlobalDB
import com.ldlda.chesscom_stats.store.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

/**
 * ViewModel for managing favorites across the app.
 * Handles all Room DB operations on background threads.
 */
class FavoritesViewModel : ViewModel() {

    private val _favorites = MutableLiveData<Set<String>>()
    val favorites: LiveData<Set<String>> = _favorites

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val dao = GlobalDB.db.favoriteDao()

    /**
     * Load all favorites from Room DB (background thread)
     */
    fun loadFavorites() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val users = withContext(Dispatchers.IO) {
                    dao.getAllUsersOnce()
                }
                _favorites.value = users.map { it.username }.toSet()
            } catch (e: Exception) {
                // Log error but don't crash
                e.printStackTrace()
                _favorites.value = emptySet()
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Check if a specific player is favorited (background thread)
     */
    fun isFavorite(playerId: Long, callback: (Boolean) -> Unit?) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                dao.existsByPlayerId(playerId)
            }
            callback(result)
        }
    }

    /**
     * Add a player to favorites (background thread)
     */
    fun addFavorite(playerId: Long, username: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = User(
                    playerId = playerId,
                    username = username,
                    favoriteSince = Date()
                )
                dao.upsert(user)
            }
            // Reload to update UI
            loadFavorites()
        }
    }

    /**
     * Remove a player from favorites (background thread)
     */
    fun removeFavorite(playerId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteByPlayerId(playerId)
            }
            // Reload to update UI
            loadFavorites()
        }
    }

    /**
     * Remove a player by username (when we don't have playerId)
     */
    fun removeFavoriteByUsername(username: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteByUsername(username)
            }
            // Reload to update UI
            loadFavorites()
        }
    }

    /**
     * Toggle favorite status (background thread)
     */
    fun toggleFavorite(playerId: Long, username: String, callback: (Boolean) -> Unit?) {
        viewModelScope.launch {
            val isFav = withContext(Dispatchers.IO) {
                val exists = dao.existsByPlayerId(playerId)
                if (exists) {
                    dao.deleteByPlayerId(playerId)
                    false
                } else {
                    dao.upsert(
                        User(
                            playerId = playerId,
                            username = username,
                            favoriteSince = Date()
                        )
                    )
                    true
                }
            }
            callback(isFav)
            loadFavorites()
        }
    }
}
