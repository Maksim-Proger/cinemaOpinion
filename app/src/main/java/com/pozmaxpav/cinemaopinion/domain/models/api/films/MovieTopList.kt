package com.pozmaxpav.cinemaopinion.domain.models.api.films

class MovieTopList(
    val pagesCount: Int,
    val films: List<MovieData.MovieTop>
)