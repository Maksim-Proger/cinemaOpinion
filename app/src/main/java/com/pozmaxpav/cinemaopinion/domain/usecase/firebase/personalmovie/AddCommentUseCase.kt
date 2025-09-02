package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String, selectedMovieId: Int, comment: DomainCommentModel) {
        repository.addComment(userId, selectedMovieId, comment)
    }
}