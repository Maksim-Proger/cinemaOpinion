package com.pozmaxpav.cinemaopinion.data.repository

import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.api.MovieListApi
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val api: MovieListApi) : MovieRepository {

    override suspend fun getPremiereMovies(year: Int, month: String): MovieList {
        return api.requestMoviesByYearAndMonth(year, month).toDomain()
    }

    override suspend fun getTopMovies(page: Int): MovieTopList {
        return api.requestTopListMovies(page).toDomain()
    }

    override suspend fun getSearchMovies(keyword: String, page: Int): MovieSearchList {
        return api.searchFilms(keyword, page).toDomain()
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