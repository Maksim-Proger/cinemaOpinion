package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class GetCommentsForMovieUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(dataSource: String, movieId: Double):  List<DomainCommentModel> {
        return firebaseRepository.getCommentsForMovie(dataSource, movieId)
    }
}
