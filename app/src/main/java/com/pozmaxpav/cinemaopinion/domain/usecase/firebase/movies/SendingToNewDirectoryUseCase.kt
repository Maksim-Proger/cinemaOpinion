package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import javax.inject.Inject

class SendingToNewDirectoryUseCase @Inject constructor(
    private val firebaseRepository: MovieRepository
) {
    suspend operator fun invoke(dataSource: String, directionDataSource: String, movieId: Double) {
        firebaseRepository.sendingToNewDirectory(dataSource, directionDataSource, movieId)
    }
}
