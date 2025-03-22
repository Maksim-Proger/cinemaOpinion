package com.pozmaxpav.cinemaopinion.data.repository.firebase

import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.RecordsOfChangesRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_CHANGES
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecordsOfChangesRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : RecordsOfChangesRepository {

    override suspend fun savingChangeRecord(domainChangelogModel: DomainChangelogModel) {
        val key = databaseReference.child(NODE_LIST_CHANGES).push().key
        key?.let {
            val record = DomainChangelogModel(
                noteId = it,
                username = domainChangelogModel.username,
                noteText = domainChangelogModel.noteText,
                timestamp = domainChangelogModel.timestamp
            )

            databaseReference.child(NODE_LIST_CHANGES).child(it).setValue(record).await()
        } ?: throw Exception("Failed to generate key")
    }

    override suspend fun getRecordsOfChanges(): List<DomainChangelogModel> {
        val snapshot = databaseReference.child(NODE_LIST_CHANGES).get().await()
        return snapshot.children.mapNotNull { childSnapshot ->
            childSnapshot.getValue(DomainChangelogModel::class.java)
        }
            .map {
                DomainChangelogModel(
                    noteId = it.noteId,
                    username = it.username,
                    noteText = it.noteText,
                    timestamp = it.timestamp
                )
            }
    }

    override suspend fun removeRecordsOfChanges(id: String) {
        try {
            val snapshot = databaseReference
                .child(NODE_LIST_CHANGES)
                .orderByKey()
                .equalTo(id)
                .get()
                .await()

            if (snapshot.exists() && snapshot.hasChildren()) {
                // Проходим по всем найденным элементам
                for (filmSnapshot in snapshot.children) {
                    filmSnapshot.ref.removeValue().await() // Удаляем запись
                }
            } else {
                // TODO: Добавить отлов ошибки
            }

        } catch (e: Exception) {
            // TODO: Добавить отлов ошибки
        }
    }

}