package com.pozmaxpav.cinemaopinion.domain.models

data class CompositeRequest(
    val type: String?,
    val keyword: String?,
    val countries: Int?,
    val genres: Int?,
    val ratingFrom: Int?,
    val yearFrom: Int?,
    val yearTo: Int?
)
