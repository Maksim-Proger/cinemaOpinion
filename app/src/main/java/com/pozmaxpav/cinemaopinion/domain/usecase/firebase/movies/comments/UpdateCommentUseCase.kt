//package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments
//
//import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
//import javax.inject.Inject
//
//class UpdateCommentUseCase @Inject constructor(
//    private val repository: MovieRepository
//) {
//    suspend operator fun invoke(dataSource: String, selectedMovieId: Int, commentId: String, selectedComment: DomainCommentModel) {
//        repository.updateComment(dataSource, selectedMovieId, commentId, selectedComment)
//    }
//}