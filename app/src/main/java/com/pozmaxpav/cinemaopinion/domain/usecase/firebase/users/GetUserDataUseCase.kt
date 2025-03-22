package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.users

import com.pozmaxpav.cinemaopinion.domain.models.firebase.User
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val firebaseRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): User? {
        return firebaseRepository.getUserData(userId)
    }
}