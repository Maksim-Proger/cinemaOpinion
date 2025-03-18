package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String): User? {
        return firebaseRepository.getUserData(userId)
    }
}