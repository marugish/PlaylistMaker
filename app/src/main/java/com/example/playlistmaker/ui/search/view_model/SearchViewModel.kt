package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.TracksState
import com.example.playlistmaker.util.SearchError

class SearchViewModel: ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private var latestSearchText: String? = null

    private val tracksInteractor = Creator.provideTracksInteractor()
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

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
                    //adapter.setItems(foundTracks)
                    //showMessage("", 0, false)
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


// ИЛИ тут необходимо обращение к application?? чтобы потом не обращаться к R.string

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun saveSearchHistory(track: Track) {
        searchHistoryInteractor.saveSearchHistory(track)
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }


}