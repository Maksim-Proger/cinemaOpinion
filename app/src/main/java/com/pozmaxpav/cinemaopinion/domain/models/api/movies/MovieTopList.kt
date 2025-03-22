package com.pozmaxpav.cinemaopinion.domain.models.api.movies

class MovieTopList(
    val pagesCount: Int,
    val films: List<MovieData.MovieTop>
)