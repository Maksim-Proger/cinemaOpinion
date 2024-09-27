package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.User

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun getUser(): User? // возвращает User?, чтобы он мог вернуть null, если пользователь не найден.
    suspend fun updateUser(user: User)
}