package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedmovie

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SelectedMovieRepository
import javax.inject.Inject

class AddMovieToPersonalListUseCase @Inject constructor(
    private val selectedMovieRepository: SelectedMovieRepository
) {
    suspend operator fun invoke(userId: String, selectedMovie: SelectedMovieModel) {
        return selectedMovieRepository.addMovieToPersonalList(userId, selectedMovie)
    }
}