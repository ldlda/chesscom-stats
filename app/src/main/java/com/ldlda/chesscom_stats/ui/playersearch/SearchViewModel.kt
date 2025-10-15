package com.ldlda.chesscom_stats.ui.playersearch

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.util.RepoProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class SearchViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val repo: ChessRepository = RepoProvider.defaultRepository(app)

    private val _data = MutableLiveData<List<SearchItem>>()
    val data: LiveData<List<SearchItem>> = _data

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable?>()
    val error: LiveData<Throwable?> = _error

    fun load(pref: String) {
        Log.d("SearchViewModel", "load: pinged with $pref")
        if (_loading.value == true) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val g = repo.searchPlayers(pref)
                _data.value = g
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
