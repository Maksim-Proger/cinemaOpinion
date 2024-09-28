package com.pozmaxpav.cinemaopinion.data.localdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_films")
data class SelectedMovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nameFilm: String
)
