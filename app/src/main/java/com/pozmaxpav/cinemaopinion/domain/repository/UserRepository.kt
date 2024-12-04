package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.DomainUser

interface UserRepository {
    suspend fun insertUser(user: DomainUser)
    suspend fun getUser(): DomainUser? // возвращает User?, чтобы он мог вернуть null, если пользователь не найден.
    suspend fun updateUser(user: DomainUser)
    suspend fun incrementSeasonalEventPoints(userId: String, increment: Long)
    suspend fun updateAwardsList(userId: String, newAwards: String)
}