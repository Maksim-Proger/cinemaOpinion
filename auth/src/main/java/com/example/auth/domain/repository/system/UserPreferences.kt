package com.example.auth.domain.repository.system

import kotlinx.coroutines.flow.StateFlow

interface UserPreferences {
    val registrationFlag: StateFlow<Boolean>
    val userId: StateFlow<String?>

    fun saveRegistrationFlag(registrationFlag: Boolean)
    fun saveUserId(userId: String)
}

