package com.pozmaxpav.cinemaopinion.domain.usecase

import com.pozmaxpav.cinemaopinion.domain.models.PagedMovieList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository

class GetTopMoviesUseCase(private val repository: MovieRepository) {
    suspend fun execute(page: Int) : PagedMovieList {
        return repository.getTopMovies(page)
    }
}