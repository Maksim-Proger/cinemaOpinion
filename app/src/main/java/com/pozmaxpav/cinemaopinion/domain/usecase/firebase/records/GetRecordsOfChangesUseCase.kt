package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class GetRecordsOfChangesUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(): List<DomainChangelogModel> {
        return firebaseRepository.getRecordsOfChanges()
    }
}