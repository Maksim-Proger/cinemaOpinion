package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedmovie

import com.pozmaxpav.cinemaopinion.domain.repository.remote.SelectedMovieRepository
import javax.inject.Inject

class DeleteMovieFromPersonalListUseCase @Inject constructor(
    private val selectedMovieRepository: SelectedMovieRepository
) {
   suspend operator fun invoke(userId: String, selectedMovieId: String) {
       selectedMovieRepository.deleteMovieFromPersonalList(userId, selectedMovieId)
   }
}