package com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol

import com.pozmaxpav.cinemaopinion.domain.models.room.models.SeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.SeriesControlRepository
import javax.inject.Inject

class SCGetMovieByIdUseCase @Inject constructor(
    private val seriesControlRepository: SeriesControlRepository
) {
    suspend operator fun invoke(id: Int): SeriesControlModel? {
        return seriesControlRepository.getMovieById(id)
    }
}