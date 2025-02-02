package com.example.playlistmaker.ui.mediaLibrary.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {


    // добавление НОВОГО плейлиста
    fun insertPlaylistToDb(newPlaylist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.insertNewPlaylist(playlist = newPlaylist)
        }
    }
}