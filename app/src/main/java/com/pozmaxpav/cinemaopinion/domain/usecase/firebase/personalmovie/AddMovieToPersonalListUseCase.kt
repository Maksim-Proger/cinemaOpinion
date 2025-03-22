package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class AddMovieToPersonalListUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String, selectedMovie: DomainSelectedMovieModel) {
        return repository.addMovieToPersonalList(userId, selectedMovie)
    }
}