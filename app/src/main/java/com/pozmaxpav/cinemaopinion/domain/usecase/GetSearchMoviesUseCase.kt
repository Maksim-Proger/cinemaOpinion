package com.pozmaxpav.cinemaopinion.domain.usecase

import com.pozmaxpav.cinemaopinion.domain.models.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class GetSearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun execute(keyword: String): MovieSearchList {
        return repository.getSearchMovies(keyword)
    }
}