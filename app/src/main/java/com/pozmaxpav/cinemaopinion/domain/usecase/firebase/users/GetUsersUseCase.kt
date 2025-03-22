package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.users

import com.pozmaxpav.cinemaopinion.domain.models.firebase.User
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val firebaseRepository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        return firebaseRepository.getUsers()
    }
}