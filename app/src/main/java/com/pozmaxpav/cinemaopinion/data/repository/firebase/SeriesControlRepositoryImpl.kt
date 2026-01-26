package com.pozmaxpav.cinemaopinion.data.repository.firebase

import android.util.Log
import com.example.core.utils.CoreDatabaseConstants.ENTRIES_KEY_LISTENER
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_USERS
import com.example.core.utils.CoreDatabaseConstants.NODE_SERIES_CONTROL
import com.example.core.utils.FirebaseListenerHolder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SeriesControlRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeriesControlRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val listenerHolder: FirebaseListenerHolder
) : SeriesControlRepository {

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
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        // 1. Находим userKey
        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with ID $userId not found")

        // 2. Ссылка на entries
        val entriesRef = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child(NODE_SERIES_CONTROL)

        // 3. Создаем listener отдельно
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val entries = snapshot.children.mapNotNull {
                    runCatching {
                        it.getValue(DomainSeriesControlModel::class.java)
                    }.getOrNull()
                }
                onEntriesUpdated(entries)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error loading entries: ${error.message}")
            }
        }

        // 4. Навешиваем listener
        entriesRef.addValueEventListener(listener)

        // 5. Регистрируем в holder
        listenerHolder.addListener(ENTRIES_KEY_LISTENER, entriesRef, listener)
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

                    if (entriesSnapshot.exists() && entriesSnapshot.hasChildren()) {
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

    override suspend fun updateEntry(
        userId: String,
        entryId: String,
        selectedEntry: DomainSeriesControlModel
    ) {
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

                    if (entriesSnapshot.exists() && entriesSnapshot.hasChildren()) {
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

    override fun removeEntriesListener() {
        listenerHolder.removeListener(ENTRIES_KEY_LISTENER)
    }

}