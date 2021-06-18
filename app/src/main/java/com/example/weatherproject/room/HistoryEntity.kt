package com.example.weatherproject.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temp: Int,
    val condition: String
)