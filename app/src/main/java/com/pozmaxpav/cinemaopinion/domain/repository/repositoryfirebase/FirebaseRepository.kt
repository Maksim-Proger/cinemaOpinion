package com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase

import com.pozmaxpav.cinemaopinion.domain.models.modelfirebase.FilmDomain

interface FirebaseRepository {
    suspend fun saveMovie(title: String)
    suspend fun removeMovie(title: String)
    suspend fun getMovie(): List<FilmDomain>
}