package com.pozmaxpav.cinemaopinion.domain.usecase.movies

import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class GetSearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun execute(keyword: String, page: Int): MovieSearchList {
        return repository.getSearchMovies(keyword, page)
    }
}