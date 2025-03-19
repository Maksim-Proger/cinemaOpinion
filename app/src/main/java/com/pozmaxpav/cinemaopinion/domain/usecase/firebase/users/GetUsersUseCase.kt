package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.users

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(): List<User> {
        return firebaseRepository.getUsers()
    }
}