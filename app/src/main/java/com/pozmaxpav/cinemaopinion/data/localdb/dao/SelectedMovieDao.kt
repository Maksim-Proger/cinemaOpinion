package com.pozmaxpav.cinemaopinion.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pozmaxpav.cinemaopinion.data.localdb.entities.SelectedMovieEntity

@Dao
interface SelectedMovieDao {

    @Insert
    suspend fun insertFilm(selectedMovie: SelectedMovieEntity)

    @Query("SELECT * FROM selected_films WHERE id = :id LIMIT 1")
    suspend fun getFilmById(id: Int): SelectedMovieEntity?

    @Query("SELECT * FROM selected_films")
    suspend fun getListSelectedFilms() : List<SelectedMovieEntity>

}