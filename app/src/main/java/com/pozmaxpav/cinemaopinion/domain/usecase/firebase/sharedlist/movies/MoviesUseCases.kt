package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.movies

import javax.inject.Inject

data class MoviesUseCases @Inject constructor(
    val addMovie: AddMovieUseCase,
    val getMovies: GetMoviesUseCase,
    val removeMovie: RemoveMovieUseCase,
    val observeListMovies: ObserveListMoviesUseCase
)
