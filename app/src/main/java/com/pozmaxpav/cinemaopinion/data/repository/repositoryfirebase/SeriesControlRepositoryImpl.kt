package com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SeriesControlRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_USERS
import com.pozmaxpav.cinemaopinion.utilits.NODE_SERIES_CONTROL
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SeriesControlRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : SeriesControlRepository {

    private var valueEventListener: ValueEventListener? = null

    override fun removeListener() {
        valueEventListener?.let { listener ->
            databaseReference.removeEventListener(listener)
            valueEventListener = null
        }
    }

    override suspend fun getListEntries(userId: String): List<DomainSeriesControlModel> {
        val entries = mutableListOf<DomainSeriesControlModel>()
        if (userId.isNotEmpty()) {
            val userSnapshot = databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("id")
                .equalTo(userId)
                .get()
                .await()

            if (userSnapshot.exists()) {
                val userKey = userSnapshot.children.firstOrNull()?.key

                if (userKey != null) {
                    val entriesSnapshot = databaseReference
                        .child(NODE_LIST_USERS)
                        .child(userKey)
                        .child(NODE_SERIES_CONTROL)
                        .get()
                        .await()

                    for (entrySnapshot in entriesSnapshot.children) {
                        val entry = entrySnapshot.getValue(DomainSeriesControlModel::class.java)
                        if (entry != null) {
                            entries.add(entry)
                        }
                    }
                }
            } else {
                throw IllegalArgumentException("User with ID $userId not found.")
            }
        }
        return entries
    }

    override suspend fun observeListEntries(
        userId: String,
        onEntriesUpdated: (List<DomainSeriesControlModel>) -> Unit
    ) {
        removeListener() // Удаляем предыдущий слушатель перед добавлением нового
        if (userId.isNotEmpty()) {
            databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("id")
                .equalTo(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userSnapshot = snapshot.children.firstOrNull()
                        valueEventListener = userSnapshot?.child(NODE_SERIES_CONTROL)?.ref?.addValueEventListener(object :
                            ValueEventListener {
                            override fun onDataChange(entrySnapshot: DataSnapshot) {
                                val entry = entrySnapshot.children.mapNotNull {
                                    it.getValue(
                                        DomainSeriesControlModel::class.java
                                    )
                                }
                                onEntriesUpdated(entry)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // TODO: Добавить отлов ошибки
                            }
                        })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // TODO: Добавить отлов ошибки
                    }
                })
        }
    }

    override suspend fun addNewEntry(userId: String, entry: DomainSeriesControlModel) {
        if (userId.isNotEmpty()) {
            val userSnapshot = databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("id")
                .equalTo(userId)
                .get()
                .await()

            if (userSnapshot.exists()) {
                val userKey = userSnapshot.children.firstOrNull()?.key
                val newEntryId = databaseReference
                    .child(NODE_LIST_USERS)
                    .child(userKey!!)
                    .child(NODE_SERIES_CONTROL)
                    .push().key!!

                databaseReference
                    .child(NODE_LIST_USERS)
                    .child(userKey)
                    .child(NODE_SERIES_CONTROL)
                    .child(newEntryId)
                    .setValue(entry)
                    .await()
            } else {
                throw IllegalArgumentException("User with ID $userId not found.")
            }
        }
    }

    override suspend fun deleteEntry(userId: String, entryId: String) {
        if (userId.isNotEmpty()) {
            val userSnapshot = databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("id")
                .equalTo(userId)
                .get()
                .await()

            if (userSnapshot.exists()) {
                val userKey = userSnapshot.children.firstOrNull()?.key

                if (userKey != null) {
                    val entriesSnapshot = databaseReference
                        .child(NODE_LIST_USERS)
                        .child(userKey)
                        .child(NODE_SERIES_CONTROL)
                        .get()
                        .await()

                    if(entriesSnapshot.exists() && entriesSnapshot.hasChildren()) {
                        for (entry in entriesSnapshot.children) {
                            if (entry.child("id").getValue(String::class.java) == entryId) {
                                entry.ref.removeValue().await()
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun updateEntry(userId: String, entryId: String, selectedEntry: DomainSeriesControlModel) {
        if (userId.isNotEmpty()) {
            val userSnapshot = databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("id")
                .equalTo(userId)
                .get()
                .await()

            if (userSnapshot.exists()) {
                val userKey = userSnapshot.children.firstOrNull()?.key

                if (userKey != null) {
                    val entriesSnapshot = databaseReference
                        .child(NODE_LIST_USERS)
                        .child(userKey)
                        .child(NODE_SERIES_CONTROL)
                        .get()
                        .await()

                    if(entriesSnapshot.exists() && entriesSnapshot.hasChildren()) {
                        for (entry in entriesSnapshot.children) {
                            if (entry.child("id").getValue(String::class.java) == entryId) {
                                entry.ref.setValue(selectedEntry).await()
                                break
                            }
                        }
                    }
                }
            }
        }
    }

}