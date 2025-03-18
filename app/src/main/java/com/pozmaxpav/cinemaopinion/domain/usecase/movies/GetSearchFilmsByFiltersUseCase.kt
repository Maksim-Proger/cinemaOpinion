package com.pozmaxpav.cinemaopinion.domain.usecase.movies

import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepository
import javax.inject.Inject

class GetSearchFilmsByFiltersUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        type: String? = null,
        keyword: String? = null,
        countries: Int? = null,
        genres: Int? = null,
        ratingFrom: Int? = null,
        yearFrom: Int? = null,
        yearTo: Int? = null,
        page: Int
    ): MovieSearchList {
        return repository.getSearchFilmsByFilters(
            type,
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