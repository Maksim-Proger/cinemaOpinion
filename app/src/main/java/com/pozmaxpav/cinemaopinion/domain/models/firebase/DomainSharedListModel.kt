package com.pozmaxpav.cinemaopinion.domain.models.firebase

data class DomainSharedListModel(
    val listId: String = "",
    val title: String = "",
    val source: String = "",
    val users: Map<String, String> = emptyMap(),
    val timestamp: Long = 0
)

data class DomainMySharedListModel(
    val listId: String = "",
    val title: String = "",
    val source: String = ""
)
