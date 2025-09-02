package com.pozmaxpav.cinemaopinion.data.api

import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieList
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieSearch
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieSearchList
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieSearchList2
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieTopList
import com.pozmaxpav.cinemaopinion.data.models.api.news.ApiNewsList
import com.pozmaxpav.cinemaopinion.utilits.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/premieres")
    suspend fun requestMoviesByYearAndMonth(
        @Query("year") year: Int,
        @Query("month") month: String
    ): ApiMovieList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/top?type=TOP_250_BEST_FILMS")
    suspend fun requestTopListMovies(
        @Query("page") page: Int
    ): ApiMovieTopList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films")
    suspend fun searchFilms(
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): ApiMovieSearchList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.1/films/search-by-keyword")
    suspend fun searchFilms2(
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): ApiMovieSearchList2

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films")
    suspend fun searchFilmsByFilters(
        @Query("type") type: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("countries") countries: Int? = null,
        @Query("genres") genres: Int? = null,
        @Query("ratingFrom") ratingFrom: Int? = null,
        @Query("yearFrom") yearFrom: Int? = null,
        @Query("yearTo") yearTo: Int? = null,
        @Query("page") page: Int
    ): ApiMovieSearchList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}")
    suspend fun getSearchMovieById(
        @Path("id") id: Int
    ): ApiMovieSearch

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v1/media_posts")
    suspend fun getMediaNews(
        @Query("page") page: Int
    ): ApiNewsList



}
