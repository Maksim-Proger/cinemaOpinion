package com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie

interface FirebaseRepository {
    suspend fun saveMovie(selectedMovie: SelectedMovie)
    suspend fun removeMovie(id: Double)
    suspend fun getMovie(): List<SelectedMovie>
}