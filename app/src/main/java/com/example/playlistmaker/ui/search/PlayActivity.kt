package com.example.playlistmaker.ui.search

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayBinding
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding

    private var mainThreadHandler: Handler? = null
    private var updateTimerTask: Runnable? = createUpdateTimerTask()

    private val getMediaPlayerInteractor = Creator.provideMediaPlayerInteractor()

    private companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val STATE_COMPLETED = 4
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
        if (track != null) {
            showTrackDetails(track)

            trackUrl = track.previewUrl
            if (trackUrl.isNotEmpty()) {
                getMediaPlayerInteractor.prepare(trackUrl)
            }
        }

        binding.playButton.setOnClickListener {
            if (trackUrl.isNotEmpty()) {
                playbackControl()
            } else {
                Toast.makeText(applicationContext, getString(R.string.no_preview_track), Toast.LENGTH_LONG).show()
            }
        }
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
        getMediaPlayerInteractor.getCurrentStateAndPosition(consumer = object : MediaPlayerInteractor.MediaPlayerConsumer {
            override fun consumeCurrentStateAndPosition(position: Int, state: Int) {
                when(state) {
                    STATE_PLAYING -> {
                        pausePlayer()
                    }
                    STATE_PREPARED, STATE_PAUSED -> {
                        startPlayer()
                    }
                }
            }
        })
    }

    private fun completedPlayer() {
        getMediaPlayerInteractor.changeState(STATE_PREPARED)
        resetToZeroPlayer()
    }

    private fun startPlayer() {
        getMediaPlayerInteractor.play()
        binding.playButton.setImageDrawable(pauseImage)
        updateTimerTask?.let { mainThreadHandler?.post(it) }
    }

    private fun createUpdateTimerTask(): Runnable {
        return Runnable {
            getMediaPlayerInteractor.getCurrentStateAndPosition(
                consumer = object : MediaPlayerInteractor.MediaPlayerConsumer {
                override fun consumeCurrentStateAndPosition(position: Int, state: Int) {
                    if (state == STATE_PLAYING) {
                        binding.playDurationTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(position)
                        updateTimerTask?.let { mainThreadHandler?.postDelayed(it, REFRESH_TIMER_DELAY_MILLIS) }
                    } else if (state == STATE_COMPLETED) {
                        completedPlayer()
                    }
                }
            })
        }
    }

    private fun pausePlayer() {
        getMediaPlayerInteractor.pause()
        binding.playButton.setImageDrawable(playImage)
        updateTimerTask?.let { mainThreadHandler?.removeCallbacks(it) }
    }
}