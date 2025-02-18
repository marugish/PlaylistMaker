package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.RootActivity
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistsState
import com.example.playlistmaker.ui.mediaLibrary.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.search.activity.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistBinding

    private val playlistId: Long? by lazy {
        arguments?.getLong("playlist")
    }
    private val viewModel: PlaylistViewModel by viewModel { parametersOf(playlistId) }

    private var tracksAdapter: TrackAdapter? = TrackAdapter { track -> showTrackPlayer(track) }

    private fun showTrackPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            val bundle = Bundle()
            bundle.putSerializable("track", track)
            findNavController().navigate(R.id.playFragment, bundle)
            (activity as RootActivity).hideOrShowBottomNavigationView(View.GONE)

            // !!!!!!!!!!!!!!!!!
            //favoritesViewModel.saveSearchHistory(track)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tracksRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.tracksRecycleView.adapter = tracksAdapter

        binding.toolbarPlaylist.setNavigationOnClickListener {
            findNavController().popBackStack()
            (activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistState.Content -> showPlaylist(it.playlist, it.tracks)
                is PlaylistState.Empty -> showEmptyList()
            }
        }

    }

    private fun showEmptyList() {
        binding.apply {
            playlistTextView.text = "Название плейлиста"
            descriptionTextView.text = "Описание"
            imageView.setImageResource(R.drawable.not_found_placeholder)



            // убрать список плейлистов
            //tracksRecycleView.visibility = View.GONE
        }
    }

    private fun showPlaylist(playlist: Playlist, tracks: List<Track>?) {
        binding.apply {
            playlistTextView.text = playlist.playlistName
            // Описание может быть пустым !!!!!!!!!
            descriptionTextView.text = playlist.playlistDescription
            Glide.with(requireContext())
                .load(playlist.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(imageView)
            // вычисление Общей продолжительности всех треков
            if (tracks != null) {
                val durationSumMillis = tracks.sumOf { it.trackTimeMillis.toLong() }
                val duration = SimpleDateFormat("mm", Locale.getDefault()).format(durationSumMillis)

                durationPlaylist.text = "$duration минут"
            }








            // отобразить список треков
            tracksRecycleView.visibility = View.VISIBLE
            if (tracks != null) {
                tracksAdapter?.setItems(tracks)
            }

        }

    }
}