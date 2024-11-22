package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.player.MediaPlayerInterface
import com.example.playlistmaker.data.player.impl.MediaPlayer
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.Storage
import com.example.playlistmaker.data.search.mapper.TracksMapper
import com.example.playlistmaker.data.search.network.ItunesApi
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.SharedPrefsStorage
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.StringProvider
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.impl.StringProviderImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    // Player
    factory {
        android.media.MediaPlayer()
    }

    factory<MediaPlayerInterface> {
        MediaPlayer(get())
    }






    // Пока не уверена с single и factory везде!!!!!!!!!!

    // Search


    // Network
    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }


    // Storage
    single {
        androidContext().getSharedPreferences("playlist_preferences", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<Storage>{
        SharedPrefsStorage(get(), get())
    }

    // TracksMapper
    single {
        TracksMapper
    }



    // Sharing
    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidApplication())//androidContext())
    }

    single<StringProvider> {
        StringProviderImpl(androidApplication())//androidContext())
    }

}