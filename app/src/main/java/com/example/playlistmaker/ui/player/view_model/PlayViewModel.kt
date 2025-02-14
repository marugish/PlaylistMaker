package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.state.PlayStatusState
import com.example.playlistmaker.ui.player.state.TrackScreenState
import com.example.playlistmaker.util.PlayerStates
import com.example.playlistmaker.util.SingleLiveEvent
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayViewModel(private val track: Track?,
                    private val mediaPlayerInteractor: MediaPlayerInteractor,
                    private val favoriteInteractor: FavoriteInteractor,
                    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
    val favorite = MutableLiveData(false)
    val playlists = MutableLiveData<List<Playlist>>(emptyList())
    val trackInPlaylist = SingleLiveEvent<Boolean>()

    private var timerJob: Job? = null

    init {
        if (track == null) {
            screenStateLiveData.postValue(TrackScreenState.Empty)
        } else {
            screenStateLiveData.postValue(TrackScreenState.Content(track, PlayStatusState.ToZero))
            likeTrack()
            prepareMediaPlayer(track.previewUrl)
        }
    }

    fun getScreenStateLiveData(): LiveData<TrackScreenState> = screenStateLiveData

    companion object {
        const val REFRESH_TIMER_DELAY_MILLIS = 500L
    }

    private fun updatePlayStatus(playStatus: PlayStatusState) {
        val currentState = screenStateLiveData.value as? TrackScreenState.Content
        if (currentState != null) {
            screenStateLiveData.value = currentState.copy(playStatus = playStatus)
        }
    }

    private fun prepareMediaPlayer(trackUrl: String) {
        if (trackUrl.isNotEmpty()) {
            mediaPlayerInteractor.prepare(trackUrl)
        } else {
            updatePlayStatus(PlayStatusState.Error)
        }
    }

    override fun onCleared() {
        releasePlayer()
    }

    private fun likeTrack() {
        viewModelScope.launch {
            favoriteInteractor.getIdFavoriteTracks().collect { trackIds ->
                processResult(trackIds)
            }
        }
    }

    private fun addTrack(foundPlaylist: Playlist, tracks: List<Long>) {
        viewModelScope.launch {
            if (track != null)
            {
                // добавляем трек в лист
                val list: MutableList<Long> = tracks.toMutableList()
                list.add(0, track.trackId)
                // Gson
                val jsonList = Gson().toJson(list)
                playlistInteractor.updatePlaylistInfoTracks(
                    foundPlaylist.id!!, jsonList, foundPlaylist.trackCount + 1)
                playlistInteractor.insertTrackInPlaylist(track)
            }

        }
    }

    fun findTrackInPlaylist(idPlaylist: Long) {
        val foundPlaylist = playlists.value?.find { it.id == idPlaylist }
        if (foundPlaylist != null) {
            val json = foundPlaylist.trackIds
            if (json.isEmpty()) {
                trackInPlaylist.postValue(false)
                // добавление трека из 2х функций
                addTrack(foundPlaylist, emptyList())
            } else {
                val tracks = Gson().fromJson(json, Array<Long>::class.java).toList()
                // поиск нужного трека
                if (tracks.contains(track?.trackId)) {
                    trackInPlaylist.postValue(true)
                } else {
                    trackInPlaylist.postValue(false)
                    // добавление трека из 2х функций
                    addTrack(foundPlaylist, tracks)
                }
            }
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                processResultPlaylists(playlists)
            }
        }
    }

    private fun processResultPlaylists(getPlaylists: List<Playlist>) {
        if (getPlaylists.isEmpty()) {
            playlists.postValue(emptyList())
        } else {
            playlists.postValue(getPlaylists)
        }
    }

    // добавление трека в Избранное
    fun addTrackToFavorite() {
        viewModelScope.launch {
            if (track != null) {
                favoriteInteractor.insertFavoriteTrack(track)
            }
        }
    }

    // удаление трека из Избранного
    fun deleteTrackFromFavorite() {
        viewModelScope.launch {
            if (track != null) {
                favoriteInteractor.deleteFavoriteTrack(track)
            }
        }
    }

    private fun processResult(trackIds: List<Long>) {
        if (track != null) {
            // поиск трека по ID в списке ID
            val isTrackContains: Boolean = trackIds.contains(track.trackId)
            if (isTrackContains) {
                favorite.postValue(true)
            } else {
                favorite.postValue(false)
            }
        }
    }

    fun playbackControl(trackUrl: String) {
        if (trackUrl.isNotEmpty()) {
            mediaPlayerInteractor.getCurrentStateAndPosition { position, state ->
                when (state) {
                    PlayerStates.PLAYING -> {
                        pausePlayer()
                    }
                    PlayerStates.PREPARED, PlayerStates.PAUSED -> {
                        startPlayer()
                    }
                    else -> {}
                }
            }
        } else {
            updatePlayStatus(PlayStatusState.Error)
        }

    }

    fun pausePlayer() {
        mediaPlayerInteractor.pause()
        updatePlayStatus(PlayStatusState.Pause)
        timerJob?.cancel()
    }

    private fun startPlayer() {
        mediaPlayerInteractor.play()
        updatePlayStatus(PlayStatusState.Start)
        startTimer()
    }

    private fun resetToZeroPlayer() {
        updatePlayStatus(PlayStatusState.ToZero)
        timerJob?.cancel()
    }

    private fun completedPlayer() {
        mediaPlayerInteractor.changeState(PlayerStates.PREPARED)
        resetToZeroPlayer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                mediaPlayerInteractor.getCurrentStateAndPosition { position, state ->
                    if (state == PlayerStates.PLAYING) {
                        updatePlayStatus(PlayStatusState.PlayState(time = position))
                    } else if (state == PlayerStates.COMPLETED) {
                        completedPlayer()
                    }
                }
                delay(REFRESH_TIMER_DELAY_MILLIS)
            }
        }
    }

    fun releasePlayer() {
        mediaPlayerInteractor.release()
        resetToZeroPlayer()
    }

    private fun renderState(state: TrackScreenState) {
        screenStateLiveData.postValue(state)
    }
}