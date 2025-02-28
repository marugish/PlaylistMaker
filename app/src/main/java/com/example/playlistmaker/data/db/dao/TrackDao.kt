package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteFavoriteTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY timestamp DESC")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM track_table")
    fun getIdFavoriteTracks(): Flow<List<Long>>
}