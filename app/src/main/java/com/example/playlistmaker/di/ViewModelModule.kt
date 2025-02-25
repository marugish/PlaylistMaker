package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.mediaLibrary.view_model.EditPlaylistViewModel
import com.example.playlistmaker.ui.mediaLibrary.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.mediaLibrary.view_model.NewPlaylistViewModel
import com.example.playlistmaker.ui.mediaLibrary.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.mediaLibrary.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.player.view_model.PlayViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // MediaLibrary
    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        FavoritesViewModel(get(), get())
    }


    // Player
    viewModel { (track: Track?) ->
        PlayViewModel(track, get(), get(), get())
    }

    // Search
    viewModel {
        SearchViewModel(get(), get())
    }

    // Settings
    viewModel {
        SettingsViewModel(get(), get())
    }

    // Create New Playlist
    viewModel {
        NewPlaylistViewModel(get())
    }

    viewModel { (id: Long?) ->
        PlaylistViewModel(id, get(), get())
    }

    // NEW
    viewModel { (playlistId: Long?) ->
        EditPlaylistViewModel(playlistId, get())
    }


}