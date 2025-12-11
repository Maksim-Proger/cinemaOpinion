package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.comments

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String, selectedMovieId: Int): List<DomainCommentModel> {
        return repository.getComments(userId, selectedMovieId)
    }
}