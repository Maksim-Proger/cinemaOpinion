package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.notification

import javax.inject.Inject

data class NotificationUseCases @Inject constructor(
    val addNotification: CreateNotificationUseCase,
    val getNotification: GetNotificationsUseCase,
    val refNotification: RemoveNotificationUseCase
)
