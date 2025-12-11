package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class AddMovieUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(listId: String, selectedMovie: DomainSelectedMovieModel) {
        repository.addMovie(listId, selectedMovie)
    }
}