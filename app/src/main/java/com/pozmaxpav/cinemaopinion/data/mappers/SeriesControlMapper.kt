package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.local.room.entities.SeriesControlEntity
import com.pozmaxpav.cinemaopinion.domain.models.room.models.SeriesControlModel

fun SeriesControlModel.toEntity(): SeriesControlEntity {
    return SeriesControlEntity(
        id = id,
        title = title,
        season = season,
        series = series
    )
}

fun SeriesControlEntity.toDomain(): SeriesControlModel {
    return SeriesControlModel(
        id = id,
        title = title,
        season = season,
        series = series
    )
}