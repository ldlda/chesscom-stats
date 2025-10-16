package com.ldlda.chesscom_stats.ui.leaderboards

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.di.RepoProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class LeaderboardsViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val repo: ChessRepository = RepoProvider.defaultRepository(app)

    private val _data = MutableLiveData<Leaderboards>()
    val data: LiveData<Leaderboards> = _data

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable?>()
    val error: LiveData<Throwable?> = _error

    fun load(refresh: Boolean = false) {
        if (_loading.value == true) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                // For now, repository-level TTL will handle freshness. If you add an explicit
                // bypass-ETag path, thread it here when refresh=true.
                val boards = repo.getLeaderboards()
                Log.i(this::class.simpleName, "load: got leaderboards")
                _data.value = boards
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
