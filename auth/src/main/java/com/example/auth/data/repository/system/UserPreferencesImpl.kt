package com.example.auth.data.repository.system

import com.example.auth.domain.repository.system.SystemRepositoryAuth
import com.example.auth.domain.repository.system.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesImpl @Inject constructor(
    private val systemRepositoryAuth: SystemRepositoryAuth
) : UserPreferences {
    private val _registrationFlag = MutableStateFlow(systemRepositoryAuth.getRegistrationFlag())
    override val registrationFlag: StateFlow<Boolean> = _registrationFlag

    private val _userId = MutableStateFlow(systemRepositoryAuth.getUserId())
    override val userId: StateFlow<String?> = _userId

    override fun saveRegistrationFlag(registrationFlag: Boolean) {
        systemRepositoryAuth.saveRegistrationFlag(registrationFlag)
        _registrationFlag.value = registrationFlag
    }

    override fun saveUserId(userId: String) {
        systemRepositoryAuth.saveUserId(userId)
        _userId.value = userId
    }
}