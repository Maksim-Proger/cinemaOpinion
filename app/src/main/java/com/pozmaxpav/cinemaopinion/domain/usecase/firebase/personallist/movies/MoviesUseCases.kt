package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.movies

import javax.inject.Inject

data class MoviesUseCases @Inject constructor(
    val addMovie: AddMovieUseCase,
    val dellMovie: DeleteMovieUseCase,
    val getMovies: GetMoviesUseCase,
    val observeList: ObserveListMoviesUseCase
)
