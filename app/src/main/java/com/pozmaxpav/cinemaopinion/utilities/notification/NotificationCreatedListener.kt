package com.pozmaxpav.cinemaopinion.utilities.notification

interface NotificationCreatedListener {
    suspend fun onNotificationCreated(userId: String, changeId: String)
}
