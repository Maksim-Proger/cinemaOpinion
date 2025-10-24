package com.example.core.domain

data class DomainUserModel(
    val id: String = "",
    val nikName: String = "",
    val email: String = "",
    val password: String = "",
    val awards: String = "",
    val professionalPoints: String = "",
    val seasonalEventPoints: Long = 0L
)