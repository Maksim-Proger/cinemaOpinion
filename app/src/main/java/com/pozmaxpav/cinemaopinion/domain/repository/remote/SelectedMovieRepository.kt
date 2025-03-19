package com.pozmaxpav.cinemaopinion.domain.repository.remote

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovie

interface SelectedMovieRepository {
    suspend fun addMovieToPersonalList(selectedMovie: SelectedMovie)
    suspend fun getListPersonalMovies(): List<SelectedMovie>
    suspend fun deleteMovieFromPersonalList(selectedMovie: SelectedMovie)
}