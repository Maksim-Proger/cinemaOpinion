package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.RecordsOfChangesRepository
import javax.inject.Inject

class RemoveRecordsOfChangesUseCase @Inject constructor(
    private val repository: RecordsOfChangesRepository
) {
    suspend operator fun invoke(id: String) {
        repository.removeRecordsOfChanges(id)
    }
}