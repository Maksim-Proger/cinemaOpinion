package com.pozmaxpav.cinemaopinion.data.repository.firebase

import android.util.Log
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_CHANGES
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_USERS
import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.NotificationRepository
import com.pozmaxpav.cinemaopinion.utilities.notification.NotificationCreatedListener
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val createdListener: NotificationCreatedListener? = null
) : NotificationRepository {

    override suspend fun createNotification(userId: String, domainNotificationModel: DomainNotificationModel) {
        try {
            val key = databaseReference
                .child(NODE_LIST_CHANGES)
                .push()
                .key ?: return // Если ключ не создался, выходим

            val record = domainNotificationModel.copy(noteId = key)

            // Сохраняем в Firebase
            databaseReference
                .child(NODE_LIST_CHANGES)
                .child(key)
                .setValue(record)
                .await()

            // Проверяем ID и уведомляем наш сервер
            if (userId.isNotBlank() && userId != "Unknown") {
                createdListener?.onNotificationCreated(userId, key)
            }
        } catch (e: Exception) {
            Log.e("NotificationError", "Failed to create notification", e)
        }
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