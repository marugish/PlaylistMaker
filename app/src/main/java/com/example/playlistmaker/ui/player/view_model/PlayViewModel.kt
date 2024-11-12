package com.example.playlistmaker.ui.player.view_model

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

class PlayViewModel(private val track: Track?): ViewModel() {

    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()

    private val screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
    private val playStatusLiveData = MutableLiveData<PlayStatus>()

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
        fun factory(track: Track?): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayViewModel(track = track)
                }
            }
        }
    }

    fun prepareMediaPlayer(trackUrl: String) {
        mediaPlayerInteractor.prepare(trackUrl)

        // тут ли мы меняем статус???
        playStatusLiveData.postValue(PlayStatus.Prepared)
    }

    override fun onCleared() {

    }

}