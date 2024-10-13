package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SwitchThemeDto
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.models.SwitchTheme
import com.example.playlistmaker.domain.models.Track

class StorageRepositoryImpl(private val storage: Storage): StorageRepository {

    override fun saveThemeParam(saveThemeParam: SwitchTheme): Boolean {
        //Log.d("sharedpref", "saveThemeParam()")
        return storage.save(mapToStorageSwitchTheme(saveThemeParam))
    }

    override fun getThemeParam(): SwitchTheme {
        //Log.d("sharedpref", "getThemeParam()")
        val theme = storage.getTheme()
        return mapToDomain(theme)
    }

    override fun saveSearchHistory(tracks: List<Track>): Boolean {
        return storage.save(mapToStorageSearchHistory(tracks))
    }

    override fun getSearchHistory(): List<Track> {
        val foundTracks = storage.getHistoryTracks()
        return mapToDomainSearchHistory(foundTracks)
    }

    // подумать над названием
    private fun mapToStorageSwitchTheme(saveThemeParam: SwitchTheme): SwitchThemeDto {
        return SwitchThemeDto(darkTheme = saveThemeParam.darkTheme)
    }

    private fun mapToDomain(theme: SwitchThemeDto): SwitchTheme {
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