package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.storage.StorageRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.search.StorageRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    // Player
    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    // Search
    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    // Storage
    single<StorageRepository>{
        StorageRepositoryImpl(get(), get())
    }

    // Settings
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

}