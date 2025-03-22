package com.pozmaxpav.cinemaopinion.domain.usecase.api.movies

import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieList
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi
import javax.inject.Inject

class GetPremiereMoviesUseCase @Inject constructor(
    private val repository: MovieRepositoryApi
) {
    suspend operator fun invoke(year: Int, month: String) : MovieList {
        return repository.getPremiereMovies(year, month)
    }
}