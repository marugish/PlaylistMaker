package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.Storage
import com.example.playlistmaker.data.dto.SwitchThemeDto
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.models.SwitchTheme
import com.example.playlistmaker.domain.models.Track

class StorageRepositoryImpl(private val storage: Storage): StorageRepository {
    private var trackCache: MutableList<Track> = getSearchHistoryTracks().toMutableList()

    override fun saveThemeParam(saveThemeParam: SwitchTheme): Boolean {
        return storage.save(mapToDataSwitchTheme(saveThemeParam))
    }

    override fun getThemeParam(): SwitchTheme {
        val theme = storage.getTheme()
        return mapToDomainSwitchTheme(theme)
    }

    override fun getSearchHistoryTracks(): List<Track> {
        trackCache = getSearchHistoryFromSharedPreferences().toMutableList()// Загрузка из SharedPreferences
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
        return storage.save(mapToStorageSearchHistory(tracks))
    }

    override fun getSearchHistoryFromSharedPreferences(): List<Track> {
        val foundTracks = storage.getHistoryTracks()
        return mapToDomainSearchHistory(foundTracks)
    }

    override fun clearHistory() {
        storage.clearHistory()
        trackCache = mutableListOf()
    }

    private fun mapToDataSwitchTheme(saveThemeParam: SwitchTheme): SwitchThemeDto {
        return SwitchThemeDto(darkTheme = saveThemeParam.darkTheme)
    }

    private fun mapToDomainSwitchTheme(theme: SwitchThemeDto): SwitchTheme {
        return SwitchTheme(darkTheme = theme.darkTheme)
    }

    private fun mapToDomainSearchHistory(tracksDto: List<TrackDto>): List<Track> {
        return tracksDto.map { Track(it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100,
            it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl) }
    }

    private fun mapToStorageSearchHistory(tracks: List<Track>): List<TrackDto> {
        return tracks.map { TrackDto(it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100,
            it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl) }
    }
}