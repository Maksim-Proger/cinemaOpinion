package com.pozmaxpav.cinemaopinion.domain.usecase.api.movies

import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepository
import javax.inject.Inject

class GetSearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(keyword: String, page: Int): MovieSearchList {
        return repository.getSearchMovies(keyword, page)
    }
}