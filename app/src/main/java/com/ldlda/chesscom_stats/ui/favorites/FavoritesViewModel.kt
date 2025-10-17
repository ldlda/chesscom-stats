package com.ldlda.chesscom_stats.ui.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ldlda.chesscom_stats.di.RepoProvider
import com.ldlda.chesscom_stats.store.GlobalDB
import com.ldlda.chesscom_stats.store.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

/**
 * ViewModel for managing favorites across the app.
 * Handles all Room DB operations on background threads.
 * Fetches player details from API to enrich display.
 */
class FavoritesViewModel : ViewModel() {

    private val _favorites = MutableLiveData<List<UserFavoriteModel>>()
    val favorites: LiveData<List<UserFavoriteModel>> = _favorites

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val dao = GlobalDB.db.favoriteDao()
    private val repo = RepoProvider.default

    companion object {
        private const val TAG = "FavoritesViewModel"
    }

    /**
     * Load all favorites from Room DB + fetch player details from API (lazy)
     * Shows usernames immediately, enriches with API data progressively
     */
    fun loadFavorites() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val users = withContext(Dispatchers.IO) {
                    dao.getAllUsersOnce()
                }

                // Show immediately with just usernames (fast)
                val initialModels = users.map { user ->
                    UserFavoriteModel(
                        username = user.username,
                        userId = user.playerId,
                        favoriteSince = user.favoriteSince.toInstant(),
                        title = null,
                        lastLoginTime = null
                    )
                }
                _favorites.value = initialModels

                // Lazy load: Fetch API details in background (slower)
                withContext(Dispatchers.IO) {
                    users.map { user ->
                        async {
                            try {
                                val player = repo.getPlayer(user.username)
                                UserFavoriteModel(
                                    username = user.username,
                                    userId = player.playerId,
                                    favoriteSince = user.favoriteSince.toInstant(),
                                    title = player.title,
                                    lastLoginTime = player.lastOnline
                                )
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed to fetch player ${user.username}: ${e.message}")
                                // Keep initial model if API fails
                                UserFavoriteModel(
                                    username = user.username,
                                    userId = user.playerId,
                                    favoriteSince = user.favoriteSince.toInstant(),
                                    title = null,
                                    lastLoginTime = null
                                )
                            }
                        }
                    }.awaitAll()
                }.also { enrichedModels ->
                    // Update with enriched data
                    _favorites.value = enrichedModels
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load favorites: ${e.message}")
                e.printStackTrace()
                _favorites.value = emptyList()
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
