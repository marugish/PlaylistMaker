package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {
    // Добавить трек в отдельную таблицу (таблица добавленных в плейлисты треков)
    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInPlaylist(track: TrackInPlaylistEntity)

    // Нужна ли сортировка????????? - скорее всего нет, если только по времени добавления
    //@Query("SELECT * FROM track_in_playlist_table ORDER BY timestamp DESC")
    //suspend fun getTracks(): List<PlaylistEntity>
}