package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList

interface MovieRepository {
    suspend fun getPremiereMovies(year: Int, month: String): MovieList
    suspend fun getTopMovies(page: Int): MovieTopList
    suspend fun getSearchMovies(keyword: String, page: Int): MovieSearchList
}