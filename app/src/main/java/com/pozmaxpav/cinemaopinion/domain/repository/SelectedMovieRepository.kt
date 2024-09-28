package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie

interface SelectedMovieRepository {
    suspend fun insertFilm(selectedMovie: SelectedMovie)
    suspend fun getListSelectedFilms(): List<SelectedMovie>
}