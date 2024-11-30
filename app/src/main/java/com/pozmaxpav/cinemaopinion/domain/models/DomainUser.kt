package com.pozmaxpav.cinemaopinion.domain.models

data class DomainUser(
    val id: String = "",
    val firstName: String,
    val lastName: String,
    val awards: String = "",
    val professionalPoints: Long = 0,
    val seasonalEventPoints: Long = 0
)
