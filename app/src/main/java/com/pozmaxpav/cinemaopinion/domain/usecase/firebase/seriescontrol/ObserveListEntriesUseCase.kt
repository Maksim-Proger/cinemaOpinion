package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SeriesControlRepository
import javax.inject.Inject

class ObserveListEntriesUseCase @Inject constructor(
    private val repository: SeriesControlRepository
) {
    suspend operator fun invoke(userId: String, onEntriesUpdated: (List<DomainSeriesControlModel>) -> Unit) {
        repository.observeListEntries(userId, onEntriesUpdated)
    }

    fun removeListener() {
        repository.removeEntriesListener()
    }
}