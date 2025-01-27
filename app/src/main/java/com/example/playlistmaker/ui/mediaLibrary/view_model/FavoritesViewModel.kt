package com.example.playlistmaker.ui.mediaLibrary.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.FavoriteInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.mediaLibrary.state.FavoriteState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteInteractor: FavoriteInteractor
): ViewModel() {
    private val favoritesStateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = favoritesStateLiveData

    init {
        // проверка на наличие избранных треков (реализация позже)
        //...

        //favoritesStateLiveData.postValue(FavoriteState.Empty)
        fillData()
    }

    fun fillData() {
        //renderState(FavoriteState.Loading)

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
            Log.i("test_favorite", "получили данные")
            renderState(FavoriteState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteState) {
        favoritesStateLiveData.postValue(state)
    }
}