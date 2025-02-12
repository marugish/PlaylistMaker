package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.ui.RootActivity
import com.example.playlistmaker.ui.mediaLibrary.state.FavoriteState
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.example.playlistmaker.ui.mediaLibrary.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.search.activity.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistsFragment: Fragment() {
    companion object {
        private const val PLAYLISTS = "playlists"

        fun newInstance(playlists: String) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putString(PLAYLISTS, playlists)
            }
        }
    }

    private val playlistsViewModel: PlaylistsViewModel by viewModel{
        parametersOf(requireArguments().getString(PLAYLISTS))
    }

    private lateinit var binding: FragmentPlaylistsBinding

    private val adapter = PlaylistAdapter { playlist ->
        //...
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistsRecycleView.layoutManager = GridLayoutManager(requireContext(),  2)

        binding.playlistsRecycleView.adapter = adapter

        binding.newPlaylistButton.setOnClickListener {
            (activity as RootActivity).hideOrShowBottomNavigationView(View.GONE)
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }

        playlistsViewModel.getPlaylists()

        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistState.Content -> showPlaylists(it.playlists)
                is PlaylistState.Empty -> showEmptyList()
            }
        }

    }

    private fun showEmptyList() {
        binding.apply {
            newPlaylistButton.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
            placeholderMessage.visibility = View.VISIBLE
            binding.placeholderMessage.text = getString(R.string.nothing_found)
            binding.placeholderImage.setImageResource(R.drawable.not_found_placeholder)

            // убрать список плейлистов
            playlistsRecycleView.visibility = View.GONE
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        binding.apply {
            newPlaylistButton.visibility = View.VISIBLE
            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE

            // отобразить список плейлистов
            playlistsRecycleView.visibility = View.VISIBLE
            adapter.setItems(playlists)

        }

    }
}