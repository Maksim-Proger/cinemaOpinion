//package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments
//
//import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
//import javax.inject.Inject
//
//class ObserveListCommentsUseCase @Inject constructor(
//    private val repository: MovieRepository
//) {
//    suspend operator fun invoke(dataSource: String, movieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit) {
//        repository.observeListComments(dataSource, movieId, onCommentsUpdated)
//    }
//
//    fun removeListener() {
//        repository.removeCommentsListener()
//    }
//}