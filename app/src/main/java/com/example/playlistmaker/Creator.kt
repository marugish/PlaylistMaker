package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.MediaPlayer
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.StorageRepositoryImpl
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefsStorage
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SwitchThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

private const val PLAYLIST_PREFERENCES = "playlist_preferences"

object Creator {
    private lateinit var appContext: Context
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        appContext = context
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun provideSharedPreferences(context: Context): SharedPreferences {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)
        }
        return sharedPreferences
    }

    private fun getStorageRepository(): StorageRepository {
        return StorageRepositoryImpl(SharedPrefsStorage(provideSharedPreferences(appContext)))
    }

    // Тёмная тема
    fun provideSwitchThemeInteractor(): SwitchThemeInteractor {
        return SwitchThemeInteractorImpl(getStorageRepository())
    }

    // История поиска
    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getStorageRepository())
    }

    // Медиаплеер
    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }
}