package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.MovieSearchList

interface MovieRepository {
    suspend fun getPremiereMovies(year: Int, month: String): MovieList
    suspend fun getTopMovies(page: Int): MovieTopList
    suspend fun getSearchMovies(keyword: String): MovieSearchList
}