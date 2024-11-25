package com.example.playlistmaker.ui.mediaLibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState

class PlaylistsViewModel(): ViewModel() {

    private val playlistsStateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = playlistsStateLiveData

    init {
        // проверка на наличие плейлистов (реализация позже)
        //...

        playlistsStateLiveData.postValue(PlaylistState.Empty)
    }
}