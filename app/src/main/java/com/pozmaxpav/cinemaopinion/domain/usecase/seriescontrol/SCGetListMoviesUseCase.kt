package com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol

import com.pozmaxpav.cinemaopinion.domain.models.SeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.SeriesControlRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SCGetListMoviesUseCase @Inject constructor(
    private val seriesControlRepository: SeriesControlRepository
) {
    suspend operator fun invoke(): Flow<List<SeriesControlModel>> {
        return seriesControlRepository.getListMovies()
    }
}