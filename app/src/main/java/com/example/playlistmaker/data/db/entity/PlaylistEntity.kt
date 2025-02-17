package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long,
    val playlistName: String,
    val playlistDescription: String?,
    val photoUrl: String?,
    val trackIds: String,
    val trackCount: Int
)