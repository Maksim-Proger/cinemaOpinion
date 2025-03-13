package com.pozmaxpav.cinemaopinion.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.pozmaxpav.cinemaopinion.data.local.room.entities.SelectedMovieEntity

@Dao
interface SelectedMovieDao {

    @Insert
    suspend fun insertFilm(selectedMovie: SelectedMovieEntity)

    @Query("SELECT * FROM selected_films WHERE id = :id LIMIT 1")
    suspend fun getFilmById(id: Int): SelectedMovieEntity?

    @Query("SELECT * FROM selected_films")
    suspend fun getListSelectedFilms() : List<SelectedMovieEntity>

    @Delete
    suspend fun deleteFilm(selectedMovie: SelectedMovieEntity)

}