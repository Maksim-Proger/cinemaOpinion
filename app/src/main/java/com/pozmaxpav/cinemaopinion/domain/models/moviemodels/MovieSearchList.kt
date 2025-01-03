package com.pozmaxpav.cinemaopinion.domain.models.moviemodels

class MovieSearchList(
    val totalPages: Int,
    val items: List<MovieData.MovieSearch>
)

class MovieSearchList2(
    val pagesCount: Int,
    val films: List<MovieData.MovieSearch2>
)