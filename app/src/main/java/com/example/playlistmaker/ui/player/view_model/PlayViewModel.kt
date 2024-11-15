package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.PlayStatus
import com.example.playlistmaker.ui.player.TrackScreenState
import com.example.playlistmaker.util.PlayerStates

class PlayViewModel(private val track: Track?): ViewModel() {

    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()

    private val screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
    private val playStatusLiveData = MutableLiveData<PlayStatus>()

    private var mainThreadHandler= Handler(Looper.getMainLooper())


    private var updateTimerTask: Runnable? = createUpdateTimerTask()

    // Проверить инициализацию
    init {
        Log.d("PlayViewModel", "init!: $track")
        if (track == null) {
            screenStateLiveData.postValue(TrackScreenState.Empty)
        } else {
            screenStateLiveData.postValue(TrackScreenState.Content(track))
        }

        // приготовить трек тут ли?

    }

    fun getScreenStateLiveData(): LiveData<TrackScreenState> = screenStateLiveData
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    companion object {
        const val REFRESH_TIMER_DELAY_MILLIS = 500L
        private val SEARCH_REQUEST_TOKEN = Any()

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
            playStatusLiveData.postValue(PlayStatus.Error)
        }



        // тут ли мы меняем статус??? нет, не тут, а когда в listener поменяется статус
        //playStatusLiveData.postValue(PlayStatus.Prepared)
    }

    override fun onCleared() {
        releasePlayer()
        updateTimerTask?.let { mainThreadHandler.removeCallbacks(it) }
        // скорее всего тут нужно будет в статус release плеер перевести
        // также скорее всего удалить callbacks
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
            playStatusLiveData.postValue(PlayStatus.Error)
        }

    }

    fun pausePlayer() {
        mediaPlayerInteractor.pause()
        playStatusLiveData.postValue(PlayStatus.Pause)

        updateTimerTask?.let { mainThreadHandler.removeCallbacks(it) }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.play()
        playStatusLiveData.postValue(PlayStatus.Start)

        updateTimerTask?.let { mainThreadHandler.post(it) }
    }

    private fun resetToZeroPlayer() {
        playStatusLiveData.postValue(PlayStatus.ToZero)
        updateTimerTask?.let { mainThreadHandler.removeCallbacks(it) }
    }

    private fun completedPlayer() {
        mediaPlayerInteractor.changeState(PlayerStates.PREPARED)
        resetToZeroPlayer()
    }

    // тут точно return?????
    private fun createUpdateTimerTask(): Runnable {
        return Runnable {
            mediaPlayerInteractor.getCurrentStateAndPosition { position, state ->
                if (state == PlayerStates.PLAYING) {
                    playStatusLiveData.postValue(PlayStatus.Play(time = position))
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
    /*private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = 0f, isPlaying = false)
    }*/

}