package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.mapper.TracksMapper
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.MediaPlayer
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.StorageRepositoryImpl
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.storage.SharedPrefsStorage
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.StorageRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl


object Creator {
    private lateinit var appContext: Context
    private lateinit var sharedPreferences: SharedPreferences
    private const val PLAYLIST_PREFERENCES = "playlist_preferences"

    fun init(context: Context) {
        appContext = context
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            networkClient = RetrofitNetworkClient(appContext),
            trackMapper = TracksMapper
        )
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun provideSharedPreferences(context: Context): SharedPreferences {
        if (!Creator::sharedPreferences.isInitialized) {
            sharedPreferences = context.getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)
        }
        return sharedPreferences
    }

    private fun getStorageRepository(): StorageRepository {
        return StorageRepositoryImpl(
            storage = SharedPrefsStorage(provideSharedPreferences(appContext)),
            trackMapper = TracksMapper
        )
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

    // Новая тёмная тема
    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(storage = SharedPrefsStorage(provideSharedPreferences(appContext)))
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    // Sharing
    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context = context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context = context), context)
    }

    // Тёмная тема - старая реализация
    /*fun provideSwitchThemeInteractor(): SwitchThemeInteractor {
        return SwitchThemeInteractorImpl(getStorageRepository())
    }*/
}