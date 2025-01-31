package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.example.playlistmaker.ui.mediaLibrary.view_model.PlaylistsViewModel
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

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }

        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistState.Content -> showPlaylists()//it.playlists)
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

            // убрать список плейлистов (реализация позже)
            // ...
        }
    }

    private fun showPlaylists(){//playlists: Playlists) {
        binding.apply {
            newPlaylistButton.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            // отобразить список плейлистов (реализация позже)
            // ...

        }

    }
}