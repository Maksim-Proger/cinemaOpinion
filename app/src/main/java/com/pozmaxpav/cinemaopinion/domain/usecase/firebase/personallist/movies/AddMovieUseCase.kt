package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class AddMovieUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String, selectedMovie: DomainSelectedMovieModel) {
        return repository.addMovie(userId, selectedMovie)
    }
}