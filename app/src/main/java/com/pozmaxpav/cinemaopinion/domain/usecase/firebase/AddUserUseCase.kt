package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(user: User) {
        repository.addUser(user)
    }
}