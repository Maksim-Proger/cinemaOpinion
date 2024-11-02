package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import javax.inject.Inject

class ObserveCommentsForMovieUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(movieId: Double, onCommentsUpdated: (List<DomainComment>) -> Unit) {
        firebaseRepository.observeCommentsForMovie(movieId, onCommentsUpdated)
    }
}