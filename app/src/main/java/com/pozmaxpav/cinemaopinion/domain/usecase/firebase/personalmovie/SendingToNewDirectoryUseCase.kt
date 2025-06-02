package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject


class SendingToNewDirectoryUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String, dataSource: String, directionDataSource: String, selectedMovieId: Int) {
        repository.sendingToNewDirectory(userId, dataSource, directionDataSource, selectedMovieId)
    }
}