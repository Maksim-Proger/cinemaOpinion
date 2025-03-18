package com.pozmaxpav.cinemaopinion.data.repository

import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.api.MovieListApi
import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.api.films.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.models.api.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val api: MovieListApi) : MovieRepository {

    override suspend fun getPremiereMovies(year: Int, month: String): MovieList {
        return api.requestMoviesByYearAndMonth(year, month).toDomain()
    }

    override suspend fun getTopMovies(page: Int): MovieTopList {
        return api.requestTopListMovies(page).toDomain()
    }

    override suspend fun getSearchMovies(
        keyword: String,
        page: Int
    ): MovieSearchList {
        return api.searchFilms(keyword, page).toDomain()
    }

    override suspend fun getSearchMovies2(
        keyword: String,
        page: Int
    ): MovieSearchList2 {
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