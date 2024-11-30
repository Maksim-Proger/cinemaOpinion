package com.pozmaxpav.cinemaopinion.data.firebase.models

data class DataUser(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val awards: String = "",
    val professionalPoints: Long = 0,
    val seasonalEventPoints: Long = 0
)