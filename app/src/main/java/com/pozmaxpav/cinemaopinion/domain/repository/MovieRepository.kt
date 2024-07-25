package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.PagedMovieList

interface MovieRepository {
    suspend fun getPremiereMovies(year: Int, month: String): MovieList
    suspend fun getTopMovies(page: Int): PagedMovieList
}