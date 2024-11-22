package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences


object Creator {
    private lateinit var appContext: Context
    private lateinit var sharedPreferences: SharedPreferences
    private const val PLAYLIST_PREFERENCES = "playlist_preferences"

    fun init(context: Context) {
        appContext = context
    }

    /*private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            networkClient = RetrofitNetworkClient(appContext),
            trackMapper = TracksMapper
        )
    }*/

    /*fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }*/

    private fun provideSharedPreferences(context: Context): SharedPreferences {
        if (!Creator::sharedPreferences.isInitialized) {
            sharedPreferences = context.getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)
        }
        return sharedPreferences
    }

    /*private fun getStorageRepository(): StorageRepository {
        return StorageRepositoryImpl(
            storage = SharedPrefsStorage(provideSharedPreferences(appContext)),
            trackMapper = TracksMapper
        )
    }*/

    // История поиска
    /*fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getStorageRepository())
    }*/

    // Медиаплеер
    /*private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }*/

    // Новая тёмная тема
    /*private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(storage = SharedPrefsStorage(provideSharedPreferences(appContext)))
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }*/

    // Sharing
   /*private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context = context)
    }
    private fun getStringProvider(context: Context): StringProvider {
        return StringProviderImpl(context = context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            getExternalNavigator(context = context),
            getStringProvider(context = context)
        )
    }*/




    // Тёмная тема - старая реализация
    /*fun provideSwitchThemeInteractor(): SwitchThemeInteractor {
        return SwitchThemeInteractorImpl(getStorageRepository())
    }*/
}