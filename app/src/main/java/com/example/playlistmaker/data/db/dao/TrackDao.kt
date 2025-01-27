package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteFavoriteTracks(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    // по-другому необходимо сделать метод
    // а что вернёт данный метод, будто просто лист идентификаторов
    // пока не уверена в возвращаемом значении
    @Query("SELECT trackId FROM track_table")
    suspend fun getIdFavoriteTracks(): List<Long>
}