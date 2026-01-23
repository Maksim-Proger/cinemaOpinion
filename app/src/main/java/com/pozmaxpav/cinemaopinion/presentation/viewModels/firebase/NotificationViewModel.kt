package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.notification.NotificationUseCases
import com.pozmaxpav.cinemaopinion.utilities.deletingOldRecords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationUseCases: NotificationUseCases
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<DomainNotificationModel>>(emptyList())
    val notifications = _notifications.asStateFlow()

    fun createNotification(
        context: Context,
        entityId: Int = 0,
        sharedListId: String = "",
        listName: String = "",
        username: String,
        stringResourceId: Int,
        title: String
    ) {
        viewModelScope.launch {
            try {
                val stringResource = context.getString(stringResourceId)

                val noteText =
                    if (listName != "") "$stringResource (в список: $listName) к: $title"
                    else "$stringResource $title"

                val note = DomainNotificationModel(
                    noteId = "", // Оставляем пустым, так как key будет сгенерирован позже
                    entityId = entityId,
                    sharedListId = sharedListId,
                    username = username,
                    noteText = noteText,
                    timestamp = System.currentTimeMillis()
                )
                notificationUseCases.addNotification(note)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getNotifications(userId: String) {
        viewModelScope.launch {
            try {
                _notifications.value = notificationUseCases.getNotification(userId)
                removeOldNotifications() // Удаляем старые записи
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun removeNotification(id: String) {
        viewModelScope.launch {
            try {
                notificationUseCases.refNotification(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Удаление записей из базы данных и списка
    private fun removeOldNotifications() {
        viewModelScope.launch {
            val currentList = _notifications.value
            val filteredList = mutableListOf<DomainNotificationModel>()

            currentList.forEach { record ->
                if (deletingOldRecords(record.timestamp)) {
                    // Удаляем запись из базы данных
                    removeNotification(record.noteId)
                } else {
                    filteredList.add(record)
                }
            }

            _notifications.value = filteredList // Обновляем список после удаления
        }
    }
}