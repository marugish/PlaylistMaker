package com.example.playlistmaker.ui.mediaLibrary.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.mediaLibrary.state.FavoriteState
import com.example.playlistmaker.ui.mediaLibrary.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.player.activity.PlayActivity
import com.example.playlistmaker.ui.search.activity.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoritesFragment: Fragment() {
    companion object {
        private const val FAVORITES = "favorite_tracks"
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance(favorites: String) = FavoritesFragment().apply {
            arguments = Bundle().apply {
                putString(FAVORITES, favorites)
            }
        }
    }

    private val favoritesViewModel: FavoritesViewModel by viewModel {
        parametersOf(requireArguments().getString(FAVORITES))
    }

    private lateinit var binding: FragmentFavoritesBinding

    private var favoriteAdapter: TrackAdapter? = TrackAdapter { track -> showTrackPlayer(track) }

    private var isClickAllowed = true

    private fun showTrackPlayer(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(requireContext(), PlayActivity::class.java)
            intent.putExtra("track", track)
            startActivity(intent)
            favoritesViewModel.saveSearchHistory(track)
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteTrackRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteTrackRecycleView.adapter = favoriteAdapter

        favoritesViewModel.fillData()

        favoritesViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is FavoriteState.Loading -> showLoading()
                is FavoriteState.Content ->  {
                    showFavorites(it.tracks)
                }
                is FavoriteState.Empty -> showEmptyList()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.fillData()
    }

    private fun showLoading() {
        binding.apply {
            favoriteTrackRecycleView.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showEmptyList() {
        binding.apply {
            favoriteTrackRecycleView.visibility = View.GONE
            placeholderImage.visibility = View.VISIBLE
            placeholderMessage.visibility = View.VISIBLE
            placeholderMessage.text = getString(R.string.nothing_found)
            placeholderImage.setImageResource(R.drawable.not_found_placeholder)
            progressBar.visibility = View.GONE
        }
    }

    private fun showFavorites(tracks: List<Track>) {
        binding.apply {
            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
            favoriteTrackRecycleView.visibility = View.VISIBLE
            favoriteAdapter?.setItems(tracks)
        }

    }
}