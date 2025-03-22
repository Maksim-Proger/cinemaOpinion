package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class DeleteMovieFromPersonalListUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
   suspend operator fun invoke(userId: String, selectedMovieId: Int) {
       repository.deleteMovieFromPersonalList(userId, selectedMovieId)
   }
}