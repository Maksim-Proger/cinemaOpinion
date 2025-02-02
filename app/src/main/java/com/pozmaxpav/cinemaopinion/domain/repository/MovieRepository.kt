package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.news.NewsList

interface MovieRepository {
    suspend fun getPremiereMovies(year: Int, month: String): MovieList
    suspend fun getTopMovies(page: Int): MovieTopList
    suspend fun getSearchMovies(keyword: String, page: Int): MovieSearchList
    suspend fun getSearchMovies2(keyword: String, page: Int): MovieSearchList2
    suspend fun getSearchFilmsByFilters(
        type: String? = null,
        keyword: String? = null,
        countries: Int? = null,
        genres: Int? = null,
        ratingFrom: Int? = null,
        yearFrom: Int? = null,
        yearTo: Int? = null,
        page: Int
    ): MovieSearchList
    suspend fun getSearchMovieById(id: Int): MovieSearch
    suspend fun getMediaNews(page:Int): NewsList
}
