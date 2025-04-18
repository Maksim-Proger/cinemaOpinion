package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import javax.inject.Inject

class ObserveCommentsForMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(dataSource: String, movieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit) {
        repository.observeCommentsForMovie(dataSource, movieId, onCommentsUpdated)
    }

    fun removeListener() {
        repository.removeCommentsSelectedMoviesListener()
    }
}