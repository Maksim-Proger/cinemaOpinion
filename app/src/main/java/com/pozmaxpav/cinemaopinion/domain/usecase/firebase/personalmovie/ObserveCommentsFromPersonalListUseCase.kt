package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class ObserveCommentsFromPersonalListUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String, selectedMovieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit) {
        repository.observeCommentsForMovieFromPersonalList(userId, selectedMovieId, onCommentsUpdated)
    }

    fun removeListener() {
        repository.removeCommentsSelectedMoviesListener()
    }
}