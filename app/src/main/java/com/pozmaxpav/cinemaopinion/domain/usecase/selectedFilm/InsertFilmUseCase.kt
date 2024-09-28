package com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.repository.SelectedMovieRepository
import javax.inject.Inject

class InsertFilmUseCase @Inject constructor(
    private val selectedMovieRepository: SelectedMovieRepository
) {
    suspend operator fun invoke(selectedMovie: SelectedMovie) {
        return selectedMovieRepository.insertFilm(selectedMovie)
    }
}