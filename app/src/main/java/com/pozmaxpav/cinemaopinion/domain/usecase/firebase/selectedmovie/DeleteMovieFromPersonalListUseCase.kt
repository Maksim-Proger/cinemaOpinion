package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedmovie

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SelectedMovieRepository
import javax.inject.Inject

class DeleteMovieFromPersonalListUseCase @Inject constructor(
    private val selectedMovieRepository: SelectedMovieRepository
) {
   suspend operator fun invoke(userId: String, selectedMovieId: Int) {
       selectedMovieRepository.deleteMovieFromPersonalList(userId, selectedMovieId)
   }
}