package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.information.ApiInformation
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.information.Information

fun ApiInformation.toDomain(): Information {
    return Information(
        description = description
    )
}