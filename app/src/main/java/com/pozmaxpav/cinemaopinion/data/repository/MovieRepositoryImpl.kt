package com.pozmaxpav.cinemaopinion.data.repository

import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.api.MovieListApi
import com.pozmaxpav.cinemaopinion.domain.models.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.PagedMovieList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val api: MovieListApi) : MovieRepository {
    override suspend fun getPremiereMovies(year: Int, month: String): MovieList {
        return api.movies(year, month).toDomain()
    }

    override suspend fun getTopMovies(page: Int): PagedMovieList {
        return api.topList(page).toDomain()
    }
}