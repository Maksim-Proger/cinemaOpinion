package com.example.auth.domain.usecases.system

import com.example.auth.domain.repository.system.SystemRepositoryAuth
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val repository: SystemRepositoryAuth
) {
    operator fun invoke(): String? {
        return repository.getUserId()
    }
}