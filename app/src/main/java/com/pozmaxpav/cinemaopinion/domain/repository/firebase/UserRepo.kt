package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.example.core.domain.DomainUserModel

interface UserRepo {
    suspend fun getUserData(userId: String): DomainUserModel?
    suspend fun updatingUserData(domainUserModel: DomainUserModel)
    suspend fun updateSpecificField(userId: String, fieldName: String, newValue: Any)
    suspend fun getUsers(): List<DomainUserModel>
}