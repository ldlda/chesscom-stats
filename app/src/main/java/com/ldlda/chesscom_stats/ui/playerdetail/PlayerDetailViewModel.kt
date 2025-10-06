package com.ldlda.chesscom_stats.ui.playerdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ldlda.chesscom_stats.api.data.Player
import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.di.RepositoryProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PlayerDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val repo: ChessRepository = RepositoryProvider.defaultRepository(app)

    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> = _player

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable?>()
    val error: LiveData<Throwable?> = _error

    fun load(username: String) {
        if (_loading.value == true) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val user = repo.getPlayer(username)
                // Fetch country and stats in parallel; they mutate the Player instance via helpers
                val fetchCountry = async { user.fetchCountryInfo(repo) }
                val fetchStats = async { user.fetchPlayerStats(repo) }
                awaitAll(fetchCountry, fetchStats)
                _player.value = user
            } catch (_: CancellationException) {
                // ignore
            } catch (t: Throwable) {
                _error.value = t
            } finally {
                _loading.value = false
            }
        }
    }
}
