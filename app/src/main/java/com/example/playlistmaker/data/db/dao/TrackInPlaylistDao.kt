package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.IntermediateEntity
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {
    // Добавить трек в отдельную таблицу (таблица добавленных в плейлисты треков)
    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInPlaylist(track: TrackInPlaylistEntity)

    // Нужна ли сортировка?
    @Query("SELECT * FROM track_in_playlist_table ORDER BY trackId DESC")
    suspend fun getTracks(): List<TrackInPlaylistEntity>

    @Query("DELETE FROM track_in_playlist_table WHERE trackId = :idTrack")
    suspend fun deleteRecord(idTrack: Long)
}