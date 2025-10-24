package com.example.auth.data.mappers

import com.example.auth.data.models.DataUserModel
import com.example.core.domain.DomainUserModel

fun DataUserModel.userToDomainModel(): DomainUserModel {
    return DomainUserModel(
        id = id,
        nikName = nikName,
        email = email,
        password = password,
        awards = awards,
        professionalPoints = professionalPoints,
        seasonalEventPoints = seasonalEventPoints
    )
}

fun DomainUserModel.userToDataModel(): DataUserModel {
    return DataUserModel(
        id = id,
        nikName = nikName,
        email = email,
        password = password,
        awards = awards,
        professionalPoints = professionalPoints,
        seasonalEventPoints = seasonalEventPoints
    )
}