package com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol

import com.pozmaxpav.cinemaopinion.domain.models.room.models.SeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.SeriesControlRepository
import javax.inject.Inject

class SCInsertUseCase @Inject constructor(
    private val seriesControlRepository: SeriesControlRepository
) {
    suspend operator fun invoke(movie: SeriesControlModel) {
        seriesControlRepository.insertNewMovie(movie)
    }
}