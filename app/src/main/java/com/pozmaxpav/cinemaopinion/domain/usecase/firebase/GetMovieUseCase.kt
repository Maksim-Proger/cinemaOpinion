package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.modelfirebase.FilmDomain
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
//    suspend operator fun invoke(): List<FilmDomain> {
//        return firebaseRepository.getMovie()
//    }
    suspend operator fun invoke(): List<SelectedMovie> {
        return firebaseRepository.getMovie()
    }
}