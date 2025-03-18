package com.pozmaxpav.cinemaopinion.domain.repository.remote

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainSeriesControlModel

interface SeriesControlRepository {
    fun removeListener()
    suspend fun addNewEntry(userId: String, entry: DomainSeriesControlModel)
    suspend fun getListEntries(userId: String): List<DomainSeriesControlModel>
    suspend fun observeListEntries(userId: String, onEntriesUpdated: (List<DomainSeriesControlModel>) -> Unit)
    suspend fun deleteEntry(userId: String, entryId: String)
    suspend fun getEntryById(id: Int): DomainSeriesControlModel?
    suspend fun updateEntry(entry: DomainSeriesControlModel)
}