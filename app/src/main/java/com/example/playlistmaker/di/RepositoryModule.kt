package com.example.playlistmaker.di

import com.example.playlistmaker.data.FavoriteRepositoryImpl
import com.example.playlistmaker.data.PlaylistRepositoryImpl
import com.example.playlistmaker.data.db.converter.PlaylistDbConvertor
import com.example.playlistmaker.data.db.converter.TrackDbConvertor
import com.example.playlistmaker.data.db.converter.TrackInPlaylistConvertor
import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.storage.StorageRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.domain.db.PlaylistRepository
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

    // Database Converter
    factory {
        TrackDbConvertor()
    }

    // Favorite
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    factory {
        PlaylistDbConvertor()
    }

    factory {
        TrackInPlaylistConvertor()
    }

    // New Playlist
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }
}