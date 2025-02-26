package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.IntermediateEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.domain.db.model.Playlist

@Dao
interface IntermediateDao {
    @Insert(entity = IntermediateEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(intermediate: IntermediateEntity)

    @Delete
    suspend fun deleteRecord(intermediate: IntermediateEntity)

    @Query("DELETE FROM intermediate_table WHERE playlistId = :idPlaylist")
    suspend fun deleteRecordByPlaylistId(idPlaylist: Long)

    @Query("SELECT COUNT(*) FROM intermediate_table WHERE trackId = :idTrack")
    suspend fun findTrack(idTrack: Long): Int
}