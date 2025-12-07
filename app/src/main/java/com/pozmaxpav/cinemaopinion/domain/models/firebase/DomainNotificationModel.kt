package com.pozmaxpav.cinemaopinion.domain.models.firebase

data class DomainNotificationModel(
    val noteId: String = "",
    val entityId: Int = 0,
    val sharedListId: String = "",
    val newDataSource: String = "",
    val username: String = "",
    val noteText: String = "",
    val timestamp: Long = 0
)
