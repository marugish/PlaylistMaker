package com.example.playlistmaker.ui.player.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayBinding
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.mediaLibrary.activity.NewPlaylistFragment
import com.example.playlistmaker.ui.mediaLibrary.activity.PlaylistAdapter
import com.example.playlistmaker.ui.player.state.PlayStatusState
import com.example.playlistmaker.ui.player.state.TrackScreenState
import com.example.playlistmaker.ui.player.view_model.PlayViewModel
import com.example.playlistmaker.util.customToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayActivity : AppCompatActivity() {

    private val track: Track? by lazy {
        IntentCompat.getSerializableExtra(intent, "track", Track::class.java)
    }
    private val viewModel: PlayViewModel by viewModel { parametersOf(track) }

    private lateinit var binding: ActivityPlayBinding

    private lateinit var tapPlaylist: String

    private val adapter = AddToPlaylistAdapter { playlist ->
        //Log.i("myPlaylist", "{${playlist.playlistName}}")
        tapPlaylist = playlist.playlistName
        playlist.id?.let { viewModel.findTrackInPlaylist(it) }
    }

    private lateinit var pauseImage: Drawable
    private lateinit var playImage: Drawable
    private lateinit var activeFavoriteImage: Drawable
    private lateinit var inactiveFavoriteImage: Drawable

    private var isFavorite = false
    private var getPlaylists: List<Playlist> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Список плейлистов
        binding.playlistsRecycleView.layoutManager = LinearLayoutManager(this)
        binding.playlistsRecycleView.adapter = adapter

        viewModel.playlists.observe(this) {
            getPlaylists = it
            Log.i("myPlaylists", "$getPlaylists")
        }

        // NEW - возможно, что частично необходимо реализацию переносить в PlayViewModel
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        // возобновляем воспроизведение
                        // ...
                        binding.overlay.visibility = View.GONE

                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        // правильное отображение списка плейлистов!!!!!!!
                        // ...
                        viewModel.getPlaylists()
                        binding.playlistsRecycleView.visibility = View.VISIBLE
                        adapter.setItems(getPlaylists)


                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        viewModel.trackInPlaylist.observe(this) {
            if (it == true) {
                // Toast
                customToast(applicationContext, layoutInflater,
                    "Трек уже добавлен в плейлист '$tapPlaylist'")
            } else {
                // Toast
                customToast(applicationContext, layoutInflater,
                    "Добавлено в плейлист '$tapPlaylist'")
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }




        // NEW
        binding.plusButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.newPlaylistButton.setOnClickListener {
            /*val createPlaylistFragment = NewPlaylistFragment()

            supportFragmentManager.commit {
                replace(R.id.fragment_container, createPlaylistFragment)
                addToBackStack("create playlist") // Optional: Add to back stack
            }*/

            // что будет с воспроизведением?
        }

        //////////



        binding.toolbarPlay.setNavigationOnClickListener {
            finish()
        }

        // Для превью трека
        pauseImage = ContextCompat.getDrawable(this, R.drawable.pause_button)!!
        playImage = ContextCompat.getDrawable(this, R.drawable.play_button)!!

        // Для Favorite
        activeFavoriteImage = ContextCompat.getDrawable(this, R.drawable.active_favorite)!!
        inactiveFavoriteImage = ContextCompat.getDrawable(this, R.drawable.inactive_favorite)!!

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

        viewModel.favorite.observe(this) {
            showFavoriteStatus(inFavorite = it)
            isFavorite = it
        }

        binding.playButton.setOnClickListener {
            if (track != null) {
                viewModel.playbackControl(track!!.previewUrl)
            }
        }

        binding.likeButton.setOnClickListener {
            isFavorite = !isFavorite
            showFavoriteStatus(inFavorite = isFavorite)
            if (isFavorite) { // добавили трек
                viewModel.addTrackToFavorite()
            } else { // удалили трек
                viewModel.deleteTrackFromFavorite()
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

    private fun showFavoriteStatus(inFavorite: Boolean) {
        if (inFavorite) {
            binding.likeButton.setImageDrawable(activeFavoriteImage)
        } else {
            binding.likeButton.setImageDrawable(inactiveFavoriteImage)
        }
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