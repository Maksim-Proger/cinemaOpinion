package com.pozmaxpav.cinemaopinion.data.api

import com.pozmaxpav.cinemaopinion.data.api.models.ApiMovieList
import com.pozmaxpav.cinemaopinion.data.api.models.ApiPagedMovieList
import com.pozmaxpav.cinemaopinion.data.api.models.ApiSearchMovieList
import com.pozmaxpav.cinemaopinion.domain.models.SearchListMovie
import com.pozmaxpav.cinemaopinion.utilits.API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MovieListApi {

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/premieres")
    suspend fun movies(
        @Query("year") year: Int,
        @Query("month") month: String
    ): ApiMovieList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/top?type=TOP_250_BEST_FILMS")
    suspend fun topList(@Query("page") page: Int): ApiPagedMovieList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films")
    suspend fun searchFilms(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1
    ): ApiSearchMovieList
}
