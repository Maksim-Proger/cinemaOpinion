package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel

interface SeriesControlRepository {
    fun removeEntriesListener()

    suspend fun addNewEntry(userId: String, entry: DomainSeriesControlModel)
    suspend fun getListEntries(userId: String): List<DomainSeriesControlModel>
    suspend fun observeListEntries(userId: String, onEntriesUpdated: (List<DomainSeriesControlModel>) -> Unit)
    suspend fun deleteEntry(userId: String, entryId: String)
    suspend fun updateEntry(userId: String, entryId: String, selectedEntry: DomainSeriesControlModel)
}