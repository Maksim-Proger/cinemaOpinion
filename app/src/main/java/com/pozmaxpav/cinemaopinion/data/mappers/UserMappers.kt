package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.localdb.entities.UserEntity
import com.pozmaxpav.cinemaopinion.domain.models.User

fun User.toEntity(): UserEntity { // Преобразование доменной модели в сущность
    return UserEntity(
        id = id,
        firstName = firstName,
        lastName = lastName
    )
}

fun UserEntity.toDomain(): User { // Преобразование сущности в доменную модель
    return User (
        id = id,
        firstName = firstName,
        lastName = lastName
    )
}