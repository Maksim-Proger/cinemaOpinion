package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import javax.inject.Inject

class SendingToTheSerialsListUseCase @Inject constructor(
    private val firebaseRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Double) {
        firebaseRepository.sendingToTheSerialsList(movieId)
    }
}