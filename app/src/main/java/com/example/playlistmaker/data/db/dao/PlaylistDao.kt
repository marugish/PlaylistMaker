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
    suspend fun insertNewPlaylist(playlist: PlaylistEntity)

    // Необходимо подумать над сортировкой и протестировать её
    @Query("SELECT * FROM playlist_table ORDER BY playlistId DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    /*@Query("SELECT trackIds FROM playlist_table WHERE playlistId = :id")
    suspend fun tracksInPlaylist(id: Long): String?*/

    // Добавление трека в плейлист и обновление количества добавленных треков
    @Query("UPDATE playlist_table SET trackIds = :trackIds, trackCount = :count WHERE playlistId = :id")
    suspend fun updatePlaylistTrackIdsAndCount(id: Long, trackIds: String, count: Int)



   /* @Transaction
    suspend fun insertAndUpdateTrackInPlaylist(id: Long, trackIds: String, count: Int, track: TrackInPlaylistEntity) {
        updatePlaylistTrackIdsAndCount(id, trackIds, count)
        insertTrackInPlaylist(track)
    }*/

}