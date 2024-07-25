package com.pozmaxpav.cinemaopinion.domain.usecase

import com.pozmaxpav.cinemaopinion.domain.models.MovieList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository

class GetPremiereMoviesUseCase(private val repository: MovieRepository) {
    suspend fun execute(year: Int, month: String) : MovieList {
        return repository.getPremiereMovies(year, month)
    }
}