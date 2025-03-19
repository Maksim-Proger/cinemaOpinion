package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedFilm

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SelectedMovieRepository
import javax.inject.Inject

class DeleteMovieFromPersonalListUseCase @Inject constructor(
    private val selectedMovieRepository: SelectedMovieRepository
) {
   suspend operator fun invoke(selectedMovie: SelectedMovie) {
       selectedMovieRepository.deleteMovieFromPersonalList(selectedMovie)
   }
}