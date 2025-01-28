package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.db.impl.FavoriteInteractorImpl
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    // Player
    factory<MediaPlayerInteractor>{
        MediaPlayerInteractorImpl(get())
    }

    // Search
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    // Settings
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    // Sharing
    single<SharingInteractor>{
        SharingInteractorImpl(get(), get())
    }

    // Favorite
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}