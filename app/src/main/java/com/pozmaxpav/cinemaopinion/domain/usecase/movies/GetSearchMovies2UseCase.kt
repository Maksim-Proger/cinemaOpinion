package com.pozmaxpav.cinemaopinion.domain.usecase.movies

import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class GetSearchMovies2UseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(keyword: String, page: Int): MovieSearchList2 {
        return repository.getSearchMovies2(keyword, page)
    }
}