package com.example.auth.domain.usecases.system

import com.example.auth.domain.repository.system.SystemRepositoryAuth
import javax.inject.Inject

class SaveUserIdUseCase @Inject constructor(
    private val repository: SystemRepositoryAuth
) {
    operator fun invoke(userId: String) {
        repository.saveUserId(userId)
    }
}