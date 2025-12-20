package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.systemlist

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SystemMovieRepo
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repo: SystemMovieRepo
) {
    suspend operator fun invoke(dataSource: String): List<DomainSelectedMovieModel> {
        return repo.getMovies(dataSource)
    }
}