package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.comments

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import javax.inject.Inject

class GetCommentsForMovieUseCase @Inject constructor(
    private val firebaseRepository: MovieRepository
) {
    suspend operator fun invoke(dataSource: String, movieId: Double):  List<DomainCommentModel> {
        return firebaseRepository.getCommentsForMovie(dataSource, movieId)
    }
}
