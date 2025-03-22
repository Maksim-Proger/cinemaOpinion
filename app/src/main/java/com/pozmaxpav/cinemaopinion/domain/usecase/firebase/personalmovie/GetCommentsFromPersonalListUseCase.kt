package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class GetCommentsFromPersonalListUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String, selectedMovieId: Int): List<DomainCommentModel> {
        return repository.getCommentsForMovieFromPersonalList(userId, selectedMovieId)
    }
}