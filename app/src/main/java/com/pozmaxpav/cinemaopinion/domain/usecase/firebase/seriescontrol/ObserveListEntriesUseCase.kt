package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SeriesControlRepository
import javax.inject.Inject

class ObserveListEntriesUseCase @Inject constructor(
    private val seriesControlRepository: SeriesControlRepository
) {
    suspend operator fun invoke(userId: String, onEntriesUpdated: (List<DomainSeriesControlModel>) -> Unit) {
        seriesControlRepository.observeListEntries(userId, onEntriesUpdated)
    }

    fun removeListener() {
        seriesControlRepository.removeListener()
    }
}