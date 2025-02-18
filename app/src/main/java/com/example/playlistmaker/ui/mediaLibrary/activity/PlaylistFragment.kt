package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.ui.RootActivity
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistsState
import com.example.playlistmaker.ui.mediaLibrary.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistBinding

    private val playlistId: Long? by lazy {
        arguments?.getLong("playlist")
    }
    private val viewModel: PlaylistViewModel by viewModel { parametersOf(playlistId) }

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

        binding.toolbarPlaylist.setNavigationOnClickListener {
            findNavController().popBackStack()
            (activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistState.Content -> showPlaylist(it.playlist)
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

    private fun showPlaylist(playlist: Playlist) {
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




            // отобразить список треков

            //tracksRecycleView.visibility = View.VISIBLE
            //adapter.setItems(tracks)

        }

    }
}