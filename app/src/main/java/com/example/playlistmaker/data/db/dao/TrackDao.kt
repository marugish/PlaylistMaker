package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.TrackEntity

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTracks(tracks: List<TrackEntity>)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteFavoriteTracks(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    // по-другому необходимо сделать метод
    // а что вернёт данный метод, будто просто лист идентификаторов
    //@Query("SELECT id FROM track_table")
    //suspend fun getIdFavoriteTracks(): List<TrackEntity>
}