package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.notification

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.NotificationRepository
import javax.inject.Inject

class RemoveNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: String) {
        repository.removeNotification(id)
    }
}