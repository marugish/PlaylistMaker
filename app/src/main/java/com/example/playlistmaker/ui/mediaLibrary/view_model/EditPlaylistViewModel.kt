package com.example.playlistmaker.ui.mediaLibrary.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.google.gson.Gson
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val playlistId: Long?,
                            private val playlistInteractor: PlaylistInteractor):
    NewPlaylistViewModel(playlistInteractor)
{
    private val _playlistData = MutableLiveData<Playlist?>()
    val playlistData: LiveData<Playlist?> get() = _playlistData

    init {
        getPlaylistById()
    }

    private fun getPlaylistById() {
        viewModelScope.launch {
            if (playlistId != null) {
                playlistInteractor.getPlaylistById(playlistId).collect { playlist ->
                    _playlistData.value = playlist
                }
            } else {
                _playlistData.value = null
            }
        }
    }

    fun updatePlaylistInDb(newPlaylist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylistInfo(
                id = newPlaylist.id!!,
                name = newPlaylist.playlistName,
                description =  newPlaylist.playlistDescription,
                photo = newPlaylist.photoUrl
                )
        }
    }




}