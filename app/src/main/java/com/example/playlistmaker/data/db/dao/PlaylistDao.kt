package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity

@Dao
interface PlaylistDao {
    // Необходимо подумать над Replace
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewPlaylist(playlist: PlaylistEntity): Long

    // Необходимо подумать над сортировкой и протестировать её
    @Query("SELECT * FROM playlist_table ORDER BY playlistId DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    // Добавление/удаление трека в/из плейлист(а) и обновление количества добавленных треков
    @Query("UPDATE playlist_table SET trackIds = :trackIds, trackCount = :count WHERE playlistId = :id")
    suspend fun updatePlaylistTrackIdsAndCount(id: Long, trackIds: String, count: Int)

    // Получение плейлиста по идентификатору
    @Query("SELECT * FROM playlist_table WHERE playlistId = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity

    // Редактирование плейлиста
    @Query("UPDATE playlist_table SET playlistName = :name, playlistDescription = :description, photoUrl = :photo WHERE playlistId = :id")
    suspend fun updatePlaylistInfo(id: Long, name: String, description: String?, photo: String?)

    // Удаление плейлиста
    @Query("DELETE FROM playlist_table WHERE playlistId = :id")
    suspend fun deletePlaylistById(id: Long)
}