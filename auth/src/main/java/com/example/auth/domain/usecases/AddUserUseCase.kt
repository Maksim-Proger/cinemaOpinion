package com.example.auth.domain.usecases

import com.example.core.domain.DomainUserModel
import com.example.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(domainUserModel: DomainUserModel) {
        repository.addUser(domainUserModel)
    }
}