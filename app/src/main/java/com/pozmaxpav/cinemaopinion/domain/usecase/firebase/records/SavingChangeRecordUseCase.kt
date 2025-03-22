package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.RecordsOfChangesRepository
import javax.inject.Inject

class SavingChangeRecordUseCase @Inject constructor(
    private val repository: RecordsOfChangesRepository
) {
    suspend operator fun invoke(domainChangelogModel: DomainChangelogModel) {
        repository.savingChangeRecord(domainChangelogModel)
    }
}