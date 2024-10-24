package com.pozmaxpav.cinemaopinion.domain.usecase.movies

import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class GetSearchFilmsByFiltersUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun execute(
        keyword: String? = null,
        countries: Int? = null,
        genres: Int? = null,
        ratingFrom: Int? = null,
        yearFrom: Int? = null,
        yearTo: Int? = null,
        page: Int
    ): MovieSearchList {
        return repository.getSearchFilmsByFilters(
            keyword,
            countries,
            genres,
            ratingFrom,
            yearFrom,
            yearTo,
            page
        )
    }
}