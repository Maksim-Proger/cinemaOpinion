package com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.modelfirebase.FilmDomain

interface FirebaseRepository {
//    suspend fun saveMovie(title: String)
    suspend fun saveMovie(selectedMovie: SelectedMovie)
    suspend fun removeMovie(id: Double)
//    suspend fun getMovie(): List<FilmDomain>
    suspend fun getMovie(): List<SelectedMovie>
}