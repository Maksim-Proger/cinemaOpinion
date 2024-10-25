package com.pozmaxpav.cinemaopinion.data.repository

import com.pozmaxpav.cinemaopinion.data.api.GetMovieInformationApi
import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.information.Information
import com.pozmaxpav.cinemaopinion.domain.repository.GetMovieInformationApiRepository
import javax.inject.Inject

class GetMovieInformationApiRepositoryImpl @Inject constructor(
    private val api: GetMovieInformationApi
) : GetMovieInformationApiRepository {
    override suspend fun getMovieInformation(movieId: Int): Information {
        return api.getMovieInformation(movieId).toDomain()
    }
}