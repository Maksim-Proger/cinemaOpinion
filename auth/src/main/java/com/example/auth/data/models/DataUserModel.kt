package com.example.auth.data.models

data class DataUserModel(
    val id: String = "",
    val nikName: String = "",
    val email: String = "",
    val password: String = "",
    val awards: String = "",
    val professionalPoints: String = "",
    val seasonalEventPoints: Long = 0L
)
