package com.pozmaxpav.cinemaopinion.data.repository.api

import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.api.MovieApi
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.models.api.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi
import javax.inject.Inject

class MovieRepositoryApiImpl @Inject constructor(private val api: MovieApi) : MovieRepositoryApi {

    override suspend fun getPremiereMovies(year: Int, month: String): MovieList {
        return api.requestMoviesByYearAndMonth(year, month).toDomain()
    }

    override suspend fun getTopMovies(page: Int): MovieTopList {
        return api.requestTopListMovies(page).toDomain()
    }

    override suspend fun getSearchMovies(keyword: String, page: Int): MovieSearchList {
        return api.searchFilms(keyword, page).toDomain()
    }

    override suspend fun getSearchMovies2(keyword: String, page: Int): MovieSearchList2 {
        return api.searchFilms2(keyword, page).toDomain()
    }

    override suspend fun getSearchFilmsByFilters(
        type: String?,
        keyword: String?,
        countries: Int?,
        genres: Int?,
        ratingFrom: Int?,
        yearFrom: Int?,
        yearTo: Int?,
        page: Int
    ): MovieSearchList {
        return api.searchFilmsByFilters(
            type,
            keyword,
            countries,
            genres,
            ratingFrom,
            yearFrom,
            yearTo,
            page
        ).toDomain()
    }

    override suspend fun getSearchMovieById(id: Int): MovieSearch {
        return api.getSearchMovieById(id).toDomain()
    }

    override suspend fun getMediaNews(page: Int): NewsList {
        return api.getMediaNews(page).toDomain()
    }

}