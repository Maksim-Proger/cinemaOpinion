package com.pozmaxpav.cinemaopinion.utilities.notification

import com.example.backend.BackendNotifyChangeCreatedUseCase

class BackendNotificationCreatedListener(
    private val backendNotifyChangeCreatedUseCase: BackendNotifyChangeCreatedUseCase
) : NotificationCreatedListener {

    override suspend fun onNotificationCreated(userId: String, changeId: String) {
        backendNotifyChangeCreatedUseCase.invoke(userId, changeId)
    }
}
