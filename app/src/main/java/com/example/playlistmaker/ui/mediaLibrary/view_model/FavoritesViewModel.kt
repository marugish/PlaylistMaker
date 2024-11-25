package com.example.playlistmaker.ui.mediaLibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.ui.mediaLibrary.state.FavoriteState

class FavoritesViewModel: ViewModel() {
    private val favoritesStateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = favoritesStateLiveData

    init {
        // проверка на наличие избранных треков (реализация позже)
        //...

        favoritesStateLiveData.postValue(FavoriteState.Empty)
    }
}