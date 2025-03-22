package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class GetListPersonalMoviesUseCase @Inject constructor(
    private val selectedMovieRepository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String): List<DomainSelectedMovieModel> {
        return selectedMovieRepository.getListPersonalMovies(userId)
    }
}