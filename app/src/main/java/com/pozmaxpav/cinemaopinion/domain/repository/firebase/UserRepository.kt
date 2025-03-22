package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserData(userId: String): User?
    suspend fun addUser(user: User)
    suspend fun checkLoginAndPassword(email: String, password: String): User?
    suspend fun updatingUserData(user: User)
    suspend fun updateSpecificField(userId: String, fieldName: String, newValue: Any)
}