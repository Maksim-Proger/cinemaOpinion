package com.pozmaxpav.cinemaopinion.domain.usecase.api.movies

import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi
import javax.inject.Inject

class GetSearchMovieByIdUseCase @Inject constructor(
    private val repository: MovieRepositoryApi
) {
    suspend operator fun invoke(id: Int): MovieSearch {
        return repository.getSearchMovieById(id)
    }
}