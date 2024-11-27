package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.ui.mediaLibrary.state.FavoriteState
import com.example.playlistmaker.ui.mediaLibrary.view_model.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoritesFragment: Fragment() {
    companion object {
        private const val FAVORITES = "favorite_tracks"

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

        favoritesViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is FavoriteState.Content -> showFavorites()
                is FavoriteState.Empty -> showEmptyList()
            }
        }
    }

    private fun showEmptyList() {
        binding.apply {
            placeholderImage.visibility = View.VISIBLE
            placeholderMessage.visibility = View.VISIBLE
            binding.placeholderMessage.text = getString(R.string.nothing_found)
            binding.placeholderImage.setImageResource(R.drawable.not_found_placeholder)

            // убрать список избранных треков (реализация позже)
            // ...
        }
    }

    private fun showFavorites() {
        binding.apply {
            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE

            // отобразить список плейлистов (реализация позже)
            // ...

        }

    }
}