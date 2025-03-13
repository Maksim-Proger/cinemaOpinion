package com.pozmaxpav.cinemaopinion.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_films")
data class SelectedMovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nameFilm: String,
    val posterUrl: String
)
