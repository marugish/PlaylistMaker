package com.example.playlistmaker.ui.mediaLibrary.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread

class PlaylistViewModel(private val id: Long?,
                        private val playlistInteractor: PlaylistInteractor,
                        private val sharingInteractor: SharingInteractor
): ViewModel() {

    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = playlistStateLiveData

    private val foundPlaylist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> = foundPlaylist

    val share = MutableLiveData<Boolean>()
    val isDeleted = MutableLiveData<Boolean>()

    init {
        getPlaylistById()
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
                processResult(null)
            }
        }
    }

    private fun processResult(playlist: Playlist?) {
        foundPlaylist.value = playlist
        if (playlist!= null) {
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
        } else {
            renderState(PlaylistState.Empty)
        }
    }

    fun deleteTrackInPlaylist(trackId: Long) {
        if (foundPlaylist.value != null) {
            val json = foundPlaylist.value?.trackIds
            val tracks = Gson().fromJson(json, Array<Long>::class.java).toList()
            // удаляем трек из листа
            val list: MutableList<Long> = tracks.toMutableList()
            list.removeAll { it == trackId }
            // Gson
            val jsonList = Gson().toJson(list)
            viewModelScope.launch {
                // обновляем данные о треках плейлиста
                playlistInteractor.updatePlaylistInfoTracks(
                    foundPlaylist.value?.id!!, jsonList, foundPlaylist.value!!.trackCount - 1
                )
                // удалить запись из Intermediate table
                playlistInteractor.deleteRecord(foundPlaylist.value?.id!!, trackId)
                // удалить информацию о треке, если он больше нигде не присутствует
                playlistInteractor.deleteTrackInfoIfNotPresent(trackId)
                // Обновляем информацию в конце
                getPlaylistById()
            }
        }
    }

    fun sharePlaylist() {
        viewModelScope.launch {
            if (id != null) {
                playlistInteractor.getPlaylistById(id).collect {
                    playlist ->
                    run {
                        var tracks = emptyList<Track>()
                        if (playlist.trackCount != 0) {
                            val json = playlist.trackIds

                            if (json.isNotEmpty()) {
                                val trackIds = Gson().fromJson(json, Array<Long>::class.java).toList()

                                playlistInteractor.getTracksInPlaylist(trackIds).collect { tracksInfo ->
                                    tracks = tracksInfo
                                }

                            }
                            sharingInteractor.sharePlaylist(playlist, tracks)
                            share.postValue(true)
                        } else {
                            share.postValue(false)
                        }
                    }

                }
            }
        }

    }

    fun deletePlaylist() {
        viewModelScope.launch {
            if (id != null) {
                try {
                    // Удаление всех треков в таблице Intermediate
                    playlistInteractor.deleteRecordByPlaylistId(id)
                    // Удаление самого плейлиста
                    playlistInteractor.deletePlaylistById(id = id)
                    // Потом информацию о треках, если их больше нигде нет
                    val json = foundPlaylist.value?.trackIds
                    if (!json.isNullOrEmpty()) {
                        val trackIds = Gson().fromJson(json, Array<Long>::class.java).toList()
                        trackIds.forEach { trackId ->
                            playlistInteractor.deleteTrackInfoIfNotPresent(trackId)
                        }
                    }
                    isDeleted.postValue(true)
                } catch (e: Exception) {
                    Log.e("myPlaylist", "Error occurred: ${e.message}")
                    isDeleted.postValue(false)
                }
            }
        }
    }

    private fun renderState(state: PlaylistState) {
        playlistStateLiveData.postValue(state)
    }

}