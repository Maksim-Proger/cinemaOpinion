package com.pozmaxpav.cinemaopinion.domain.usecase.movies

import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class GetPremiereMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(year: Int, month: String) : MovieList {
        return repository.getPremiereMovies(year, month)
    }
}