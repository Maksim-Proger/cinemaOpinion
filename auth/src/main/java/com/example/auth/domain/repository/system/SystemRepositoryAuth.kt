package com.example.auth.domain.repository.system

interface SystemRepositoryAuth {
    fun saveRegistrationFlag(registrationFlag: Boolean)
    fun getRegistrationFlag(): Boolean
    fun saveUserId(userId: String)
    fun getUserId(): String?
}