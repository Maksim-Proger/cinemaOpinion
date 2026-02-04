package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.notification.NotificationUseCases
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetDeviceRegistrationStatusUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetPushTokenUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveDeviceRegistrationStatusUseCase
import com.pozmaxpav.cinemaopinion.utilities.deletingOldRecords
import com.pozmaxpav.cinemaopinion.utilities.notification.DeviceDataCreatedListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationUseCases: NotificationUseCases,
    private val getPushTokenUseCase: GetPushTokenUseCase,
    private val createdListener: DeviceDataCreatedListener,
    private val saveDeviceRegistrationStatusUseCase: SaveDeviceRegistrationStatusUseCase,
    private val getDeviceRegistrationStatusUseCase: GetDeviceRegistrationStatusUseCase
) : ViewModel() {

    // region Push
    private val _statusReg = MutableStateFlow(false)
    val statusReg = _statusReg.asStateFlow()

    fun getStatus() {
        viewModelScope.launch {
            try {
                _statusReg.value = getDeviceRegistrationStatusUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun registerDevice(userId: String, deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Сначала дожидаемся получения токена (вызываем UseCase напрямую)
                val token = getPushTokenUseCase()
                if (!token.isNullOrBlank()) {
                    // 2. Отправляем на бэкенд
                    createdListener.onDataDeviceCreated(userId, token, deviceId)

                    // 3. ТОЛЬКО ЕСЛИ ОТПРАВКА ПРОШЛА УСПЕШНО — сохраняем статус
                    // Вызываем метод репозитория напрямую или через другой метод
                    saveDeviceRegistrationStatusUseCase(true)

                    // Обновляем StateFlow для UI
                    _statusReg.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // endregion

    private val _notifications = MutableStateFlow<List<DomainNotificationModel>>(emptyList())
    val notifications = _notifications.asStateFlow()

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

    fun createNotification(
        userId: String,
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
                notificationUseCases.addNotification(userId, note)
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