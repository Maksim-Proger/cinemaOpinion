package com.pozmaxpav.cinemaopinion.domain.usecase.movies.information

import com.pozmaxpav.cinemaopinion.domain.models.api.information.Information
import com.pozmaxpav.cinemaopinion.domain.repository.GetMovieInformationApiRepository
import javax.inject.Inject

class GetMovieInformationUseCase @Inject constructor(
    private val repository: GetMovieInformationApiRepository
) {
    suspend operator fun invoke(movieId: Int): Information { // TODO: Почему тут надо operate а в других нет?
        return repository.getMovieInformation(movieId)
    }
}