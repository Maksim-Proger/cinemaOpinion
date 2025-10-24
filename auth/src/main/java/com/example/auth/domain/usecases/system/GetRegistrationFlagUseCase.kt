package com.example.auth.domain.usecases.system

import com.example.auth.domain.repository.system.SystemRepositoryAuth
import javax.inject.Inject

class GetRegistrationFlagUseCase @Inject constructor(
    private val repository: SystemRepositoryAuth
) {
    suspend operator fun invoke(): Boolean {
        return repository.getRegistrationFlag()
    }
}