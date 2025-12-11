package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.movies

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class DeleteMovieUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
   suspend operator fun invoke(userId: String, selectedMovieId: Int) {
       repository.deleteMovie(userId, selectedMovieId)
   }
}