package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SwitchThemeDto
import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.models.SwitchTheme

class StorageRepositoryImpl(private val storage: Storage): StorageRepository {
    override fun saveThemeParam(saveThemeParam: SwitchTheme): Boolean {
        return storage.save(mapToStorage(saveThemeParam))
    }

    override fun getThemeParam(): SwitchTheme {
        val theme = storage.getTheme()
        return mapToDomain(theme)
    }

    private fun mapToStorage(saveThemeParam: SwitchTheme): SwitchThemeDto {
        return SwitchThemeDto(darkTheme = saveThemeParam.darkTheme)
    }

    private fun mapToDomain(theme: SwitchThemeDto): SwitchTheme {
        return SwitchTheme(darkTheme = theme.darkTheme)
    }
}