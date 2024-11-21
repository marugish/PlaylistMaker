package com.example.playlistmaker.data.search.storage

import com.example.playlistmaker.data.search.Storage
import com.example.playlistmaker.data.search.mapper.TracksMapper
import com.example.playlistmaker.domain.search.StorageRepository
import com.example.playlistmaker.domain.search.model.Track

class StorageRepositoryImpl(private val storage: Storage, private val trackMapper: TracksMapper) :
    StorageRepository {
    private var trackCache: MutableList<Track> = getSearchHistoryTracks().toMutableList()

    override fun saveThemeParam(saveThemeParam: Boolean): Boolean {
        return storage.save(saveThemeParam)
    }

    override fun getThemeParam(): Boolean {
        return storage.getTheme()
    }

    override fun getSearchHistoryTracks(): List<Track> {
        trackCache =
            getSearchHistoryFromSharedPreferences().toMutableList()// Загрузка из SharedPreferences
        return trackCache
    }

    override fun addSearchHistoryTrack(track: Track) {
        val position = trackCache.indexOf(track) // Проверяем, есть ли этот трек в списке
        if (position != -1)
            trackCache.removeAt(position) // удаляем найденный трек
        if (trackCache.size == 10) { // должно быть не больше 10 треков
            trackCache.removeLast()
        }
        trackCache.add(0, track) // теперь только добавляем в начало

        saveSearchHistoryToSharedPreferences(trackCache)
    }

    override fun saveSearchHistoryToSharedPreferences(tracks: List<Track>): Boolean {
        return storage.save(tracks.map(trackMapper::mapToStorage))
    }

    override fun getSearchHistoryFromSharedPreferences(): List<Track> {
        val foundTracks = storage.getHistoryTracks()
        return foundTracks.map(trackMapper::mapToDomain)
    }

    override fun clearHistory() {
        storage.clearHistory()
        trackCache = mutableListOf()
    }
}