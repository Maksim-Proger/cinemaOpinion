package com.pozmaxpav.cinemaopinion.domain.usecase

import com.pozmaxpav.cinemaopinion.domain.models.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository

class GetTopMoviesUseCase(private val repository: MovieRepository) {
    suspend fun execute(page: Int) : MovieTopList {
        return repository.getTopMovies(page)
    }
}