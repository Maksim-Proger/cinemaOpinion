package com.pozmaxpav.cinemaopinion.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series_control")
data class SeriesControlEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val season: Int = 0,
    val series: Int = 0
)

