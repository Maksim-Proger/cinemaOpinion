package com.pozmaxpav.cinemaopinion.domain.usecase.api.movies

import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi
import javax.inject.Inject

class GetTopMoviesUseCase @Inject constructor(
    private val repository: MovieRepositoryApi
) {
    suspend operator fun invoke(page: Int) : MovieTopList {
        return repository.getTopMovies(page)
    }
}