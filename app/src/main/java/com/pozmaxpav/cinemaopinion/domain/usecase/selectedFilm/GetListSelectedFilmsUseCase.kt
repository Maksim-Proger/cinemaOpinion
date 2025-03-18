package com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SelectedMovieRepository
import javax.inject.Inject

class GetListSelectedFilmsUseCase @Inject constructor(
    private val selectedMovieRepository: SelectedMovieRepository
) {
    suspend operator fun invoke(): List<SelectedMovie> {
        return selectedMovieRepository.getListSelectedFilms()
    }
}