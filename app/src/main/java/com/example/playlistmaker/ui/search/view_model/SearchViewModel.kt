package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.state.HistoryState
import com.example.playlistmaker.ui.search.state.TracksState
import com.example.playlistmaker.util.SearchError
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor,
                      private val searchHistoryInteractor: SearchHistoryInteractor): ViewModel() {

    private var latestSearchText: String? = null

    // Обычный поиск
    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    // История поиска
    private val historyStateLiveData = MutableLiveData<HistoryState>()
    fun observeHistoryState(): LiveData<HistoryState> = historyStateLiveData

    private var searchJob: Job? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun searchRequest(request: String) {
        if (request.isNotEmpty()) {
            renderState(TracksState.Loading)

            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(expression = request)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: SearchError?) {
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

    fun clearHistorySearch() {
        searchHistoryInteractor.clearHistory()
        renderState(HistoryState.Clear)
    }

    fun getHistorySearch() {
        searchHistoryInteractor.getSearchHistory{ results ->
            renderState(HistoryState.Content(results))
        }
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
    }

}