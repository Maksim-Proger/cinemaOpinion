package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel

interface SystemMovieRepo {
    suspend fun getMovies(dataSource: String, ): List<DomainSelectedMovieModel>
}