package com.pozmaxpav.cinemaopinion.domain.repository.api

import com.pozmaxpav.cinemaopinion.domain.models.api.information.Information

interface GetMovieInformationApiRepository {
    suspend fun getMovieInformation(movieId: Int): Information
}