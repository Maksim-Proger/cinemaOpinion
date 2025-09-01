package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class AddCommentToMovieUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(listId: String, movieId: Int, comment: DomainCommentModel) {
        repository.addCommentToMovie(listId, movieId, comment)
    }
}