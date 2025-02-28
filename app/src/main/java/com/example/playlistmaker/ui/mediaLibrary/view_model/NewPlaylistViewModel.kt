package com.example.playlistmaker.ui.mediaLibrary.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.model.Playlist
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    val idPlaylist = MutableLiveData(-1L)
    // добавление НОВОГО плейлиста
    fun insertPlaylistToDb(newPlaylist: Playlist) {
        viewModelScope.launch {
            val id = playlistInteractor.insertNewPlaylist(playlist = newPlaylist)
            idPlaylist.postValue(id)
        }
    }

}