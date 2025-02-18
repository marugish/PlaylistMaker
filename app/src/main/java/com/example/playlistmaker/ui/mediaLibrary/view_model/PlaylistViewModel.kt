package com.example.playlistmaker.ui.mediaLibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.google.gson.Gson
import kotlinx.coroutines.delay
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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun getPlaylistById() {
        viewModelScope.launch {
            if (id != null) {
                playlistInteractor.getPlaylistById(id).collect {
                    playlist -> processResult(playlist)
                }
            } else {
                renderState(PlaylistState.Empty)
            }
        }
    }

    private fun processResult(playlist: Playlist) {
        val json = playlist.trackIds
        if (json.isEmpty()) {
            renderState(PlaylistState.Content(playlist, null))
        } else {
            val trackIds = Gson().fromJson(json, Array<Long>::class.java).toList()
            viewModelScope.launch {
                playlistInteractor.getTracksInPlaylist(trackIds).collect {
                    tracksInfo -> renderState(PlaylistState.Content(playlist, tracksInfo))
                }
            }
        }
    }

    private fun renderState(state: PlaylistState) {
        playlistStateLiveData.postValue(state)
    }

}