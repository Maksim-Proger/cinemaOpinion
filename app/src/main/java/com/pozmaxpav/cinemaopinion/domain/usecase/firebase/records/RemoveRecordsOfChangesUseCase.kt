package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records

import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class RemoveRecordsOfChangesUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(id: String) {
        firebaseRepository.removeRecordsOfChanges(id)
    }
}