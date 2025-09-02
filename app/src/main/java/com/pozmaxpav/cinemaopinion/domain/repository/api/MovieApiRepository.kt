package com.pozmaxpav.cinemaopinion.domain.repository.api

import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.models.api.news.NewsList

interface MovieRepositoryApi {
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
