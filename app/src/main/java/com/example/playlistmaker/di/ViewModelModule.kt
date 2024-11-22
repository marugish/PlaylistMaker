package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.mediaLibrary.view_model.MediaLibraryViewModel
import com.example.playlistmaker.ui.player.view_model.PlayViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // MediaLibrary
    viewModel {
        MediaLibraryViewModel()
    }

    // Player
    viewModel { (track: Track?) ->
        PlayViewModel(track, get())
    }

    // trackId же где-то ещё передаётся, надо проверить

    // Search
    viewModel {
        SearchViewModel(get(), get())
    }

    // Settings
    viewModel {
        SettingsViewModel(get(), get())
    }

}