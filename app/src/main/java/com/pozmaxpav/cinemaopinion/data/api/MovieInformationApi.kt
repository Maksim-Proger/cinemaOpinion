package com.pozmaxpav.cinemaopinion.data.api

import com.pozmaxpav.cinemaopinion.data.models.api.information.ApiInformation
import com.pozmaxpav.cinemaopinion.utilits.Constants.API_KEY_DEV
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MovieInformationApi {

    @Headers("X-API-KEY: $API_KEY_DEV")
    @GET("/v1.4/movie/{id}")
    suspend fun getMovieInformation(
        @Path("id") movieId: Int
    ): ApiInformation
}