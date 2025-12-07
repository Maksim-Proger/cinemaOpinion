package com.pozmaxpav.cinemaopinion.data.repository.firebase

import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_CHANGES
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_USERS
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.NotificationRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : NotificationRepository {

    override suspend fun createNotification(domainNotificationModel: DomainNotificationModel) {
        val key = databaseReference.child(NODE_LIST_CHANGES).push().key
        key?.let {
            val record = DomainNotificationModel(
                noteId = it,
                entityId = domainNotificationModel.entityId,
                sharedListId = domainNotificationModel.sharedListId,
                newDataSource = domainNotificationModel.newDataSource,
                username = domainNotificationModel.username,
                noteText = domainNotificationModel.noteText,
                timestamp = domainNotificationModel.timestamp
            )
            databaseReference.child(NODE_LIST_CHANGES).child(it).setValue(record).await()
        } ?: throw Exception("Failed to generate key")
    }

    override suspend fun getNotifications(userId: String): List<DomainNotificationModel> {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()
            ?.key
            ?: throw IllegalArgumentException("User with ID $userId not found.")

        val sharedListIds = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child("my_shared_list")
            .get()
            .await()
            .children
            .mapNotNull { it.child("listId").getValue(String::class.java) }
            .toSet()

        if (sharedListIds.isEmpty()) return emptyList()

        val notifications = databaseReference
            .child(NODE_LIST_CHANGES)
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(DomainNotificationModel::class.java) }

        return notifications.filter { it.sharedListId in sharedListIds }
    }

//    override suspend fun getNotifications(): List<DomainNotificationModel> {
//        val snapshot = databaseReference.child(NODE_LIST_CHANGES).get().await()
//        return snapshot.children.mapNotNull { childSnapshot ->
//            childSnapshot.getValue(DomainNotificationModel::class.java)
//        }
//            .map {
//                DomainNotificationModel(
//                    noteId = it.noteId,
//                    entityId = it.entityId,
//                    sharedListId = it.sharedListId,
//                    newDataSource = it.newDataSource,
//                    username = it.username,
//                    noteText = it.noteText,
//                    timestamp = it.timestamp
//                )
//            }
//    }

    override suspend fun removeNotification(id: String) {
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