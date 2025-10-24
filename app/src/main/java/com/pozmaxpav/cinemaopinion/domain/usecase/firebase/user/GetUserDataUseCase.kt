package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user

import com.example.core.domain.DomainUserModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepo
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val firebaseRepository: UserRepo
) {
    suspend operator fun invoke(userId: String): DomainUserModel? {
        return firebaseRepository.getUserData(userId)
    }
}