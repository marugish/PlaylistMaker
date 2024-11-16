package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.state.PlayStatusState
import com.example.playlistmaker.ui.player.state.TrackScreenState
import com.example.playlistmaker.util.PlayerStates

class PlayViewModel(private val track: Track?): ViewModel() {

    private val screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
    private val playStatusStateLiveData = MutableLiveData<PlayStatusState>()

    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()

    private var mainThreadHandler= Handler(Looper.getMainLooper())
    private var updateTimerTask: Runnable? = createUpdateTimerTask()

    init {
        if (track == null) {
            screenStateLiveData.postValue(TrackScreenState.Empty)
        } else {
            screenStateLiveData.postValue(TrackScreenState.Content(track))
        }
    }

    fun getScreenStateLiveData(): LiveData<TrackScreenState> = screenStateLiveData
    fun getPlayStatusLiveData(): LiveData<PlayStatusState> = playStatusStateLiveData

    companion object {
        const val REFRESH_TIMER_DELAY_MILLIS = 500L

        fun factory(track: Track?): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayViewModel(track = track)
                }
            }
        }
    }

    fun prepareMediaPlayer(trackUrl: String) {
        if (trackUrl.isNotEmpty()) {
            mediaPlayerInteractor.prepare(trackUrl)
        } else {
            playStatusStateLiveData.postValue(PlayStatusState.Error)
        }
    }

    override fun onCleared() {
        releasePlayer()
        updateTimerTask?.let { mainThreadHandler.removeCallbacks(it) }
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
            playStatusStateLiveData.postValue(PlayStatusState.Error)
        }

    }

    fun pausePlayer() {
        mediaPlayerInteractor.pause()
        playStatusStateLiveData.postValue(PlayStatusState.Pause)

        updateTimerTask?.let { mainThreadHandler.removeCallbacks(it) }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.play()
        playStatusStateLiveData.postValue(PlayStatusState.Start)

        updateTimerTask?.let { mainThreadHandler.post(it) }
    }

    private fun resetToZeroPlayer() {
        playStatusStateLiveData.postValue(PlayStatusState.ToZero)
        updateTimerTask?.let { mainThreadHandler.removeCallbacks(it) }
    }

    private fun completedPlayer() {
        mediaPlayerInteractor.changeState(PlayerStates.PREPARED)
        resetToZeroPlayer()
    }

    private fun createUpdateTimerTask(): Runnable {
        return Runnable {
            mediaPlayerInteractor.getCurrentStateAndPosition { position, state ->
                if (state == PlayerStates.PLAYING) {
                    playStatusStateLiveData.postValue(PlayStatusState.PlayState(time = position))
                    updateTimerTask?.let {
                        mainThreadHandler.postDelayed(it, REFRESH_TIMER_DELAY_MILLIS)
                    }
                } else if (state == PlayerStates.COMPLETED) {
                    completedPlayer()
                }
            }
        }
    }

    fun releasePlayer() {
        mediaPlayerInteractor.release()
        resetToZeroPlayer()
    }

}