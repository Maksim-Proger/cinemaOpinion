package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.RecordsOfChangesRepository
import javax.inject.Inject

class GetRecordsOfChangesUseCase @Inject constructor(
    private val repository: RecordsOfChangesRepository
) {
    suspend operator fun invoke(): List<DomainChangelogModel> {
        return repository.getRecordsOfChanges()
    }
}