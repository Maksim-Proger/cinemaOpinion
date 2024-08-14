package com.pozmaxpav.cinemaopinion.domain.usecase

import com.pozmaxpav.cinemaopinion.domain.models.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository

class GetSearchMoviesUseCase(private val repository: MovieRepository) {
    suspend fun execute(keyword: String): MovieSearchList {
        return repository.getSearchMovies(keyword)
    }
}