package com.example.auth.domain.repository

import com.example.core.domain.DomainUserModel

interface AuthRepository {
    suspend fun addUser(domainUserModel: DomainUserModel)
    suspend fun authorization(email: String, password: String): DomainUserModel?
    suspend fun checkUser(email: String): Boolean
}