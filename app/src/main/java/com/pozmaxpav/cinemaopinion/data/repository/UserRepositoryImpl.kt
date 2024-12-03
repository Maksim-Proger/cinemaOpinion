package com.pozmaxpav.cinemaopinion.data.repository

import com.pozmaxpav.cinemaopinion.data.localdb.dao.UserDao
import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.mappers.toEntity
import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun insertUser(user: DomainUser) {
        userDao.insertUser(user.toEntity()) // Преобразование в сущность
    }

    override suspend fun getUser(): DomainUser {
        return userDao.getUser().toDomain() // Преобразование в доменную модель
    }

    override suspend fun updateUser(user: DomainUser) {
        userDao.updateUser(user.toEntity())
    }

    override suspend fun incrementSeasonalEventPoints(
        userId: String,
        increment: Long
    ) {
        userDao.incrementSeasonalEventPoints(userId, increment)
    }
}