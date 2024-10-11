package com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol

import com.pozmaxpav.cinemaopinion.domain.models.SeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.SeriesControlRepository
import javax.inject.Inject

class SCUpdateMovieUseCase @Inject constructor(
    private val seriesControlRepository: SeriesControlRepository
) {
    suspend operator fun invoke(movie: SeriesControlModel) {
        seriesControlRepository.updateMovie(movie)
    }
}