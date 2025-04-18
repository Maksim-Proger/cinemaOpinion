package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SeriesControlRepository
import javax.inject.Inject

class UpdateEntryUseCase @Inject constructor(
    private val seriesControlRepository: SeriesControlRepository
) {
    suspend operator fun invoke(userId: String, entryId: String, entry: DomainSeriesControlModel) {
        seriesControlRepository.updateEntry(userId, entryId, entry)
    }
}