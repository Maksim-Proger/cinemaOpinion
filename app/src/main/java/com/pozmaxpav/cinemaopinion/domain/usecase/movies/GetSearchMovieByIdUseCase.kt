package com.pozmaxpav.cinemaopinion.domain.usecase.movies

import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class GetSearchMovieByIdUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: Int): MovieSearch {
        return repository.getSearchMovieById(id)
    }
}