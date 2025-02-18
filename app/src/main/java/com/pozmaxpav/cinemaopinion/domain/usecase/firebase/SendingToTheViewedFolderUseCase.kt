package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import javax.inject.Inject

class SendingToTheViewedFolderUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(dataSource: String, directionDataSource: String, movieId: Double) {
        firebaseRepository.sendingToTheViewedFolder(dataSource, directionDataSource, movieId)
    }
}
