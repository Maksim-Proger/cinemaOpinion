package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol

import com.pozmaxpav.cinemaopinion.domain.repository.remote.SeriesControlRepository
import javax.inject.Inject

class DeleteEntryUseCase @Inject constructor(
    private val seriesControlRepository: SeriesControlRepository
) {
    suspend operator fun invoke(userId: String, entryId: String) {
        return seriesControlRepository.deleteEntry(userId, entryId)
    }
}