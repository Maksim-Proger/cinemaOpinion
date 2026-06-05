package com.pozmaxpav.cinemaopinion.domain.repository.api

import androidx.paging.PagingData
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.SearchRequest
import kotlinx.coroutines.flow.Flow

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

    fun getTopMoviesStream(): Flow<PagingData<MovieData>>
    fun getSearchStream(request: SearchRequest): Flow<PagingData<MovieData>>
}
