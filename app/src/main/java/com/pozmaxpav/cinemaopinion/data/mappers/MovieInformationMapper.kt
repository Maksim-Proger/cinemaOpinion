package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.models.api.information.ApiInformation
import com.pozmaxpav.cinemaopinion.domain.models.api.information.Information

fun ApiInformation.toDomain(): Information {
    return Information(
        description = description
    )
}