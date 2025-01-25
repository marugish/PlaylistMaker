package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.state.PlayStatusState
import com.example.playlistmaker.ui.player.state.TrackScreenState
import com.example.playlistmaker.util.PlayerStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayViewModel(private val track: Track?, private val mediaPlayerInteractor: MediaPlayerInteractor): ViewModel() {

    private val screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)

    private var timerJob: Job? = null

    init {
        if (track == null) {
            screenStateLiveData.postValue(TrackScreenState.Empty)
        } else {
            screenStateLiveData.postValue(TrackScreenState.Content(track, PlayStatusState.ToZero))
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

}