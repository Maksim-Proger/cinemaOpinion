package com.pozmaxpav.cinemaopinion.domain.usecase.movies

import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class GetPremiereMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun execute(year: Int, month: String) : MovieList {
        return repository.getPremiereMovies(year, month)
    }
}