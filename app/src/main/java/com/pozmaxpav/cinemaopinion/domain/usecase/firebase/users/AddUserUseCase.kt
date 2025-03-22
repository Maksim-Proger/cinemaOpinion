package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.users

import com.pozmaxpav.cinemaopinion.domain.models.firebase.User
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.addUser(user)
    }
}