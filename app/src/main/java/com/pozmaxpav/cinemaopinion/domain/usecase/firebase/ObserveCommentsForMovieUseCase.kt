package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class ObserveCommentsForMovieUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(dataSource: String, movieId: Double, onCommentsUpdated: (List<DomainCommentModel>) -> Unit) {
        firebaseRepository.observeCommentsForMovie(dataSource, movieId, onCommentsUpdated)
    }
}