package com.example.playlistmaker.ui.mediaLibrary.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.mediaLibrary.state.FavoriteState
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteInteractor: FavoriteInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
): ViewModel() {
    private val favoritesStateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = favoritesStateLiveData

    init {
        fillData()
    }

    fun fillData() {
        viewModelScope.launch {
            favoriteInteractor.getFavoriteTracks().collect { tracks ->
                processResult(tracks)
            }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteState.Empty)
        } else {
            renderState(FavoriteState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteState) {
        favoritesStateLiveData.postValue(state)
    }

    fun saveSearchHistory(track: Track) {
        searchHistoryInteractor.saveSearchHistory(track)
    }
}