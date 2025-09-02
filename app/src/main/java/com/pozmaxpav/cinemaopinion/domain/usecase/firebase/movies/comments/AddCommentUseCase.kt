package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val firebaseRepository: MovieRepository
) {
    suspend operator fun invoke(dataSource: String, movieId: Double, comment: DomainCommentModel) {
        firebaseRepository.addComment(dataSource, movieId, comment)
    }
}