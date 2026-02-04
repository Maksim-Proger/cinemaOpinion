package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel

interface NotificationRepository {
    suspend fun createNotification(userId: String, domainNotificationModel: DomainNotificationModel)
    suspend fun getNotifications(userId: String): List<DomainNotificationModel>
    suspend fun removeNotification(id: String)
}