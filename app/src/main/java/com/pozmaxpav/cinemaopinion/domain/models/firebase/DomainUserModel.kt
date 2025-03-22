package com.pozmaxpav.cinemaopinion.domain.models.firebase

data class User(
    val id: String = "",
    val nikName: String = "",
    val email: String = "",
    val password: String = "",
    val awards: String = "",
    val professionalPoints: String = "",
    val seasonalEventPoints: Long = 0L
)