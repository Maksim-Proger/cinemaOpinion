package com.pozmaxpav.cinemaopinion.domain.models

class PagedMovieList(
    val pagesCount: Int,
    val films: List<MovieData.MovieTopList>
)