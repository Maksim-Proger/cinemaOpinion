package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class UpdatingUserDataUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(user: User) {
        repository.updatingUserData(user)
    }
}