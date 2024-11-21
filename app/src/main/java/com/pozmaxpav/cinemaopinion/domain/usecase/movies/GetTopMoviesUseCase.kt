package com.pozmaxpav.cinemaopinion.domain.usecase.movies

import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class GetTopMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int) : MovieTopList {
        return repository.getTopMovies(page)
    }
}