package com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SeriesControlRepository
import javax.inject.Inject

class SCGetMovieByIdUseCase @Inject constructor(
    private val seriesControlRepository: SeriesControlRepository
) {
    suspend operator fun invoke(id: Int): DomainSeriesControlModel? {
        return seriesControlRepository.getEntryById(id)
    }
}