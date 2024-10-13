package com.example.playlistmaker.ui.search

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayActivity : AppCompatActivity() {

    private var mediaPlayer = MediaPlayer()

    private companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val REFRESH_TIMER_DELAY_MILLIS = 500L // 500 миллисекунд == 0,5 секунды
    }

    private var playerState = STATE_DEFAULT

    private lateinit var playDuration: TextView
    private lateinit var playButton: ImageButton
    private lateinit var pauseImage: Drawable
    private lateinit var playImage: Drawable

    private var mainThreadHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val toolbarBack = findViewById<Toolbar>(R.id.toolbar_play)
        toolbarBack.setNavigationOnClickListener {
            finish()
        }

        val track = IntentCompat.getSerializableExtra(intent, "track", Track::class.java)

        val artistName = findViewById<TextView>(R.id.singerTextView)
        val trackName = findViewById<TextView>(R.id.trackTextView)
        val trackDuration = findViewById<TextView>(R.id.durationEditText)
        val trackImage = findViewById<ImageView>(R.id.trackImageView)
        val trackAlbum = findViewById<TextView>(R.id.albumEditText)
        val trackYear = findViewById<TextView>(R.id.yearEditText)
        val trackGenre = findViewById<TextView>(R.id.genreEditText)
        val trackCountry = findViewById<TextView>(R.id.countryText)

        // Для превью трека
        var trackUrl = ""
        playButton = findViewById(R.id.playButton)
        playDuration = findViewById(R.id.playDurationTextView)
        pauseImage = ContextCompat.getDrawable(this, R.drawable.pause_button)!!
        playImage = ContextCompat.getDrawable(this, R.drawable.play_button)!!

        if (track != null) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
            trackDuration.text = formattedTime
            fun getCoverArtwork() = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
            Glide.with(this)
                .load(getCoverArtwork())
                .centerCrop()
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.placeholder)
                .into(trackImage)
            trackAlbum.text = track.collectionName
            val formatter = SimpleDateFormat("yyyy", Locale.getDefault())
            val date: Date? = formatter.parse(track.releaseDate)
            val year = date?.let { formatter.format(it) } // Форматирование Date в год
            trackYear.text = year
            trackGenre.text = track.primaryGenreName
            trackCountry.text = track.country

            trackUrl = track.previewUrl
            if (trackUrl.isNotEmpty()) {
                preparePlayer(trackUrl)
            }
        }

        playButton.setOnClickListener {
            if (trackUrl.isNotEmpty()) {
                playbackControl()
            } else {
                Toast.makeText(applicationContext, getString(R.string.no_preview_track), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
        playButton.setImageDrawable(playImage)
        playDuration.text = getString(R.string.play_time)
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageDrawable(playImage)
            playDuration.text = getString(R.string.play_time)
            playerState = STATE_PREPARED
            mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
        }
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageDrawable(pauseImage)
        playerState = STATE_PLAYING
        mainThreadHandler?.post(createUpdateTimerTask())
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    // Текущая позиция трека
                    val timePosition = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    playDuration.text = timePosition
                    mainThreadHandler?.postDelayed(this, REFRESH_TIMER_DELAY_MILLIS)
                }
            }
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageDrawable(playImage)
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
    }
}