package com.pozmaxpav.cinemaopinion.domain.usecase

import com.pozmaxpav.cinemaopinion.domain.models.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.PagedMovieList
import com.pozmaxpav.cinemaopinion.domain.models.SearchList
import com.pozmaxpav.cinemaopinion.domain.models.SearchListMovie
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository

class GetSearchMoviesUseCase(private val repository: MovieRepository) {
    suspend fun execute(keyword: String): SearchList {
        return repository.getSearchMovies(keyword)
    }
}