package com.example.playlistmaker.ui.mediaLibrary.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel(private val id: Long?,
                        private val playlistInteractor: PlaylistInteractor,
                        //private val sharingInteractor: SharingInteractor
): ViewModel() {
    // новый State надо задать
    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = playlistStateLiveData

    private val foundPlaylist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> = foundPlaylist

    val share = MutableLiveData(false)

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

    private fun getPlaylistById() {
        viewModelScope.launch {
            if (id != null) {
                playlistInteractor.getPlaylistById(id).collect {
                    playlist -> processResult(playlist)
                //foundPlaylist.value= playlist //processResult(playlist)
                }
                //processResult(foundPlaylist.value)
            } else {
                processResult(null)
                //foundPlaylist.value = null
                //processResult(foundPlaylist.value)//renderState(PlaylistState.Empty)
            }
        }
    }

    private fun processResult(playlist: Playlist?) {
        foundPlaylist.value= playlist
        Log.i("myPlaylist", "refresh ${foundPlaylist.value}")
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
        //getPlaylistById() // обновляем плейлист
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
                // ...
            }
        }

        // в конце необходимо обновить информацию, как о плейлисте, так и в UI

        getPlaylistById() // обновляем плейлист
    }

    /*fun sharePlaylist() {
        viewModelScope.launch {
            if (id != null) {
                playlistInteractor.getPlaylistById(id).collect {
                    playlist ->
                    run {
                        if (playlist.trackCount != 0) {

                            share.postValue(true)
                        } else {
                            share.postValue(false)
                        }
                    }

                }
            }
        }
        // как будто бы надо ещё раз запросить информацию о плейлисте, чтобы была обновленная информация
        //sharingInteractor.sharePlaylist()
    }*/

    fun deletePlaylist() {
        viewModelScope.launch {
            if (id != null) {
                playlistInteractor.deletePlaylistById(id = id)
                // необходимо также удалить всю информацию о плейлистах треках из 2х таблиц
                // .......
            }
        }
    }

    private fun renderState(state: PlaylistState) {
        playlistStateLiveData.postValue(state)
    }

}