package com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SelectedMovieRepository
import javax.inject.Inject

class GetFilmByIdUseCase @Inject constructor(
    private val selectedMovieRepository: SelectedMovieRepository
) {
    suspend operator fun invoke(id: Int): SelectedMovie? {
        return selectedMovieRepository.getFilmById(id)
    }
}