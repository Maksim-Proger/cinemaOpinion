package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.notification

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.NotificationRepository
import javax.inject.Inject

class CreateNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(userId: String, domainNotificationModel: DomainNotificationModel) {
        repository.createNotification(userId, domainNotificationModel)
    }
}