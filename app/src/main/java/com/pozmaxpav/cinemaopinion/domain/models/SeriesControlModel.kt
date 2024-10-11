package com.pozmaxpav.cinemaopinion.domain.models

data class SeriesControlModel(
    val id: Int,
    val title: String,
    val season: Int = 0,
    val series: Int = 0
)