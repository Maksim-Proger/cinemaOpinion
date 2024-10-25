package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.information.Information

interface GetMovieInformationApiRepository {
    suspend fun getMovieInformation(movieId: Int): Information
}