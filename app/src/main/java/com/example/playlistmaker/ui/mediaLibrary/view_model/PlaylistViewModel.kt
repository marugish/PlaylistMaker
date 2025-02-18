package com.example.playlistmaker.ui.mediaLibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistViewModel(private val id: Long?,
                        private val playlistInteractor: PlaylistInteractor): ViewModel() {
    // новый State надо задать
    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = playlistStateLiveData

    init {
        getPlaylistById()
        playlistStateLiveData.postValue(PlaylistState.Empty)
    }

    fun getPlaylistById() {
        viewModelScope.launch {
            if (id != null) {
                playlistInteractor.getPlaylistById(id).collect {
                    playlist -> processResult(playlist)
                }
            }
        }
    }

    private fun processResult(playlist: Playlist) {
        // а если пустота.....
        // ..........
        renderState(PlaylistState.Content(playlist))

    }

    private fun renderState(state: PlaylistState) {
        playlistStateLiveData.postValue(state)
    }

}