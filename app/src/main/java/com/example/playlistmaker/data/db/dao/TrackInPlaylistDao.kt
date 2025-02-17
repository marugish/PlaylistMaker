package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {
    // Добавить трек в отдельную таблицу (таблица добавленных в плейлисты треков)
    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInPlaylist(track: TrackInPlaylistEntity)
}