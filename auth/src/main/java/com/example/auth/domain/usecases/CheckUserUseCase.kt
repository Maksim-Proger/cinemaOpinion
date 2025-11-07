package com.example.auth.domain.usecases

import com.example.auth.domain.repository.AuthRepository
import javax.inject.Inject

class CheckUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String) : Boolean {
        return repository.checkUser(email)
    }
}