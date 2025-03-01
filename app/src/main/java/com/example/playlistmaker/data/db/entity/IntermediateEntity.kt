package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intermediate_table",
    primaryKeys = ["playlistId", "trackId"])
data class IntermediateEntity(
    val playlistId: Long,
    val trackId: Long
)
