package com.example.playlistmaker.ui.player.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.state.PlayStatusState
import com.example.playlistmaker.ui.player.state.TrackScreenState
import com.example.playlistmaker.ui.player.view_model.PlayViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayActivity : AppCompatActivity() {
    //private lateinit var viewModel: PlayViewModel
    /*private val track: Track? by lazy {
        IntentCompat.getSerializableExtra(intent, "track", Track::class.java)
    }*/

    private val track: Track? by lazy {
        IntentCompat.getSerializableExtra(intent, "track", Track::class.java)
    }
    private val viewModel: PlayViewModel by viewModel { parametersOf(track) }

    //private lateinit var viewModel: PlayViewModel
    //private lateinit var track: Track?

    private lateinit var binding: ActivityPlayBinding

    private lateinit var pauseImage: Drawable
    private lateinit var playImage: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarPlay.setNavigationOnClickListener {
            finish()
        }

        // Для превью трека
        pauseImage = ContextCompat.getDrawable(this, R.drawable.pause_button)!!
        playImage = ContextCompat.getDrawable(this, R.drawable.play_button)!!

        //val track = IntentCompat.getSerializableExtra(intent, "track", Track::class.java)
        //viewModel = ViewModelProvider(this, PlayViewModel.factory(track))[PlayViewModel::class.java]
        /*viewModel = viewModel {
            parametersOf(track)
        }*/

        //val track = IntentCompat.getSerializableExtra(intent, "track", Track::class.java)
        //viewModel = viewModel { parametersOf(track) }

        var isContentStateHandled = false
        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is TrackScreenState.Loading -> {
                    showLoading(loading = true)
                }
                is TrackScreenState.Content -> {
                    val contentTrack = screenState.track
                    val playStatus = screenState.playStatus

                    if (!isContentStateHandled) {
                        isContentStateHandled = true
                        showLoading(loading = false)
                        showTrackDetails(track = contentTrack)
                    }

                    when (playStatus) {
                        is PlayStatusState.Pause -> {
                            binding.playButton.setImageDrawable(playImage)
                        }
                        is PlayStatusState.Error -> {
                            binding.playButton.isEnabled = false
                            binding.playButton.alpha = 0.5f
                            Toast.makeText(applicationContext, getString(R.string.no_preview_track), Toast.LENGTH_LONG).show()
                        }
                        is PlayStatusState.Start -> {
                            binding.playButton.setImageDrawable(pauseImage)
                        }
                        is PlayStatusState.ToZero -> {
                            binding.playButton.setImageDrawable(playImage)
                            binding.playDurationTextView.text = getString(R.string.play_time)
                        }
                        is PlayStatusState.PlayState -> {
                            binding.playDurationTextView.text =
                                SimpleDateFormat("mm:ss", Locale.getDefault()).format(playStatus.time)
                        }
                    }
                }
                is TrackScreenState.Empty -> {
                    finish()
                    Toast.makeText(applicationContext, getString(R.string.load_error), Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.playButton.setOnClickListener {
            if (track != null) {
                viewModel.playbackControl(track!!.previewUrl)
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
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }
}