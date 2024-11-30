package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.localdb.entities.UserEntity
import com.pozmaxpav.cinemaopinion.domain.models.DomainUser

fun DomainUser.toEntity(): UserEntity { // Преобразование доменной модели в сущность
    return UserEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        awards = awards,
        professionalPoints = professionalPoints,
        seasonalEventPoints = seasonalEventPoints
    )
}

fun UserEntity.toDomain(): DomainUser { // Преобразование сущности в доменную модель
    return DomainUser (
        id = id,
        firstName = firstName,
        lastName = lastName,
        awards = awards,
        professionalPoints = professionalPoints,
        seasonalEventPoints = seasonalEventPoints
    )
}