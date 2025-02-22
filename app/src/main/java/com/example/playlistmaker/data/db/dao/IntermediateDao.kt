package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.IntermediateEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity

@Dao
interface IntermediateDao {
    @Insert(entity = IntermediateEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(intermediate: IntermediateEntity)

    /*@Query("DELETE FROM intermediate_table WHERE playlistId = :idPlaylist AND trackId = :idTrack")
    suspend fun deleteRecord(idPlaylist: Long, idTrack: Long)*/

    @Delete
    suspend fun deleteRecord(intermediate: IntermediateEntity)

    //@Query("SELECT * FROM track_in_playlist_table ORDER BY trackId DESC")
    //suspend fun findTrack(): List<TrackInPlaylistEntity>
}