package com.example.playlistmaker.ui.player.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityPlayBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.PlayStatus
import com.example.playlistmaker.ui.player.TrackScreenState
import com.example.playlistmaker.ui.player.view_model.PlayViewModel
import com.example.playlistmaker.util.PlayerStates
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayActivity : ComponentActivity() {
    private lateinit var viewModel: PlayViewModel
    private lateinit var binding: ActivityPlayBinding

    private var mainThreadHandler: Handler? = null
    private var updateTimerTask: Runnable? = createUpdateTimerTask()

    private val getMediaPlayerInteractor = Creator.provideMediaPlayerInteractor()

    private companion object {
        const val REFRESH_TIMER_DELAY_MILLIS = 500L // 500 миллисекунд == 0,5 секунды
    }

    private lateinit var pauseImage: Drawable
    private lateinit var playImage: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val toolbarBack = findViewById<Toolbar>(R.id.toolbar_play)
        toolbarBack.setNavigationOnClickListener {
            finish()
        }

        // Для превью трека
        var trackUrl = ""
        pauseImage = ContextCompat.getDrawable(this, R.drawable.pause_button)!!
        playImage = ContextCompat.getDrawable(this, R.drawable.play_button)!!

        val track = IntentCompat.getSerializableExtra(intent, "track", Track::class.java)
        viewModel = ViewModelProvider(this, PlayViewModel.factory(track))[PlayViewModel::class.java]

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is TrackScreenState.Loading -> {
                    showLoading(loading = true)
                }
                is TrackScreenState.Content -> {
                    showLoading(loading = false)
                    if (track != null) {
                        showTrackDetails(track = screenState.track)

                        // !!!!
                        trackUrl = screenState.track.previewUrl
                        if (trackUrl.isNotEmpty()) {
                            viewModel.prepareMediaPlayer(trackUrl)
                        }
                        // !!!

                    }
                }
                is TrackScreenState.Error -> {

                }
                is TrackScreenState.Empty -> {

                    // должен быть трек с заглушками в полях
                }
            }
        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            when (playStatus) {
                is PlayStatus.Prepared -> {
                    // поменять состояние кнопки
                    // ...
                }

            }
            //changeButtonStyle(playStatus)
            // 2
            //binding.seekBar.value = playStatus.progress
        }



        /*if (track != null) {
            showTrackDetails(track)

            trackUrl = track.previewUrl
            if (trackUrl.isNotEmpty()) {
                getMediaPlayerInteractor.prepare(trackUrl)
            }
        }*/

        binding.playButton.setOnClickListener {
            if (trackUrl.isNotEmpty()) {
                playbackControl()
            } else {
                Toast.makeText(applicationContext, getString(R.string.no_preview_track), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
    }

    private fun showTrackDetails(track: Track) {
        binding.trackTextView.text = track.trackName
        binding.artistTextView.text = track.artistName
        binding.durationEditText.text = SimpleDateFormat("mm:ss",
            Locale.getDefault()).format(track.trackTimeMillis.toLong())
        fun getCoverArtwork() = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
        Glide.with(this)
            .load(getCoverArtwork())
            .centerCrop()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.placeholder)
            .into(binding.trackImageView)
        binding.albumEditText.text = track.collectionName
        val formatter = SimpleDateFormat("yyyy", Locale.getDefault())
        val date: Date? = formatter.parse(track.releaseDate)
        val year = date?.let { formatter.format(it) } // Форматирование Date в год
        binding.yearEditText.text = year
        binding.genreEditText.text = track.primaryGenreName
        binding.countryText.text = track.country
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        getMediaPlayerInteractor.release()
        resetToZeroPlayer()
    }

    private fun resetToZeroPlayer() {
        binding.playButton.setImageDrawable(playImage)
        binding.playDurationTextView.text = getString(R.string.play_time)
        updateTimerTask?.let { mainThreadHandler?.removeCallbacks(it) }
    }

    private fun playbackControl() {
        getMediaPlayerInteractor.getCurrentStateAndPosition { position, state ->
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
    }

    private fun completedPlayer() {
        getMediaPlayerInteractor.changeState(PlayerStates.PREPARED)
        resetToZeroPlayer()
    }

    private fun startPlayer() {
        getMediaPlayerInteractor.play()
        binding.playButton.setImageDrawable(pauseImage)
        updateTimerTask?.let { mainThreadHandler?.post(it) }
    }

    private fun createUpdateTimerTask(): Runnable {
        return Runnable {
            getMediaPlayerInteractor.getCurrentStateAndPosition { position, state ->
                if (state == PlayerStates.PLAYING) {
                    binding.playDurationTextView.text =
                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(position)
                    updateTimerTask?.let {
                        mainThreadHandler?.postDelayed(
                            it,
                            REFRESH_TIMER_DELAY_MILLIS
                        )
                    }
                } else if (state == PlayerStates.COMPLETED) {
                    completedPlayer()
                }
            }
        }
    }

    private fun pausePlayer() {
        getMediaPlayerInteractor.pause()
        binding.playButton.setImageDrawable(playImage)
        updateTimerTask?.let { mainThreadHandler?.removeCallbacks(it) }
    }
}