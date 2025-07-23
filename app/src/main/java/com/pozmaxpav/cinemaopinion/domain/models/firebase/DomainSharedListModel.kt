package com.pozmaxpav.cinemaopinion.domain.models.firebase

data class DomainSharedListModel(
    val listId: String = "",
    val title: String = "",
    val source: String = "",
    val users: String = "",
    val timestamp: Long = 0
)

data class DomainMySharedListModel(
    val listId: String = "",
    val title: String = "",
    val source: String = ""
)
