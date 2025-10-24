package com.example.auth.domain.usecases.system

import com.example.auth.domain.repository.system.SystemRepositoryAuth
import javax.inject.Inject

class SaveRegistrationFlagUseCase @Inject constructor(
    private val repository: SystemRepositoryAuth
) {
    suspend operator fun invoke(registrationFlag: Boolean) {
        repository.saveRegistrationFlag(registrationFlag)
    }
}