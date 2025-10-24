package com.example.auth.domain.usecases

import com.example.core.domain.DomainUserModel
import com.example.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthorizationUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): DomainUserModel? {
        return repository.authorization(email, password)
    }
}