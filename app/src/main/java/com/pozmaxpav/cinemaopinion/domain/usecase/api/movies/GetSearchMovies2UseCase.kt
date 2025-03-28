package com.pozmaxpav.cinemaopinion.domain.usecase.api.movies

import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi
import javax.inject.Inject

class GetSearchMovies2UseCase @Inject constructor(
    private val repository: MovieRepositoryApi
) {
    suspend operator fun invoke(keyword: String, page: Int): MovieSearchList2 {
        return repository.getSearchMovies2(keyword, page)
    }
}