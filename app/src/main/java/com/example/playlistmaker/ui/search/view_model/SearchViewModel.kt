package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.state.HistoryState
import com.example.playlistmaker.ui.search.state.TracksState
import com.example.playlistmaker.util.SearchError

class SearchViewModel(private val tracksInteractor: TracksInteractor,
                      private val searchHistoryInteractor: SearchHistoryInteractor): ViewModel() {

    private var latestSearchText: String? = null

    // Обычный поиск
    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    // История поиска
    private val historyStateLiveData = MutableLiveData<HistoryState>()
    fun observeHistoryState(): LiveData<HistoryState> = historyStateLiveData

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun searchRequest(request: String) {
        if (request.isNotEmpty()) {
            renderState(TracksState.Loading)

            tracksInteractor.searchTracks(expression = request)
            { foundTracks, errorMessage ->
                val tracks = mutableListOf<Track>()
                if (foundTracks != null) {
                    tracks.addAll(foundTracks)
                }
                when {
                    errorMessage != null -> {
                        when (errorMessage) {
                            SearchError.NO_RESULTS -> {
                                renderState(TracksState.Empty(message = errorMessage))
                            }
                            SearchError.NETWORK_ERROR -> {
                                renderState(TracksState.Error(errorMessage = errorMessage))
                            }
                        }
                    }
                    else -> {
                        renderState(TracksState.Content(tracks = tracks))
                    }
                }
            }
        }
    }

    fun clearHistorySearch() {
        searchHistoryInteractor.clearHistory()
        renderState(HistoryState.Clear)
    }

    fun getHistorySearch() {
        searchHistoryInteractor.getSearchHistory{ results ->
            renderState(HistoryState.Content(results))
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun saveSearchHistory(track: Track) {
        searchHistoryInteractor.saveSearchHistory(track)
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    private fun renderState(historyState: HistoryState) {
        historyStateLiveData.postValue(historyState)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

}