package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.localdb.entities.SelectedMovieEntity
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie

fun SelectedMovie.toEntity(): SelectedMovieEntity {
    return SelectedMovieEntity(
        id = id,
        nameFilm = nameFilm,
        posterUrl = posterUrl
    )
}

fun SelectedMovieEntity.toDomain(): SelectedMovie {
    return SelectedMovie(
        id = id,
        nameFilm = nameFilm,
        posterUrl = posterUrl
    )
}

fun List<SelectedMovieEntity>.toDomain(): List<SelectedMovie> {
    return this.map { it.toDomain() }
}

/**
 * Объяснение:
 * Функция toDomain() для одного объекта преобразует SelectedMovieEntity в SelectedMovie.
 * Функция toDomain() для списка применяет преобразование ко всем элементам списка с помощью
 * функции map, которая проходит по каждому объекту в списке и вызывает для него toDomain().
 */