package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel

interface RecordsOfChangesRepository {
    suspend fun savingChangeRecord(domainChangelogModel: DomainChangelogModel)
    suspend fun getRecordsOfChanges(): List<DomainChangelogModel>
    suspend fun removeRecordsOfChanges(id: String)
}