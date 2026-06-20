package com.pozmaxpav.cinemaopinion.domain.usecase.api.movies

import androidx.paging.PagingData
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.SearchRequest
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchMoviesPagingUseCase @Inject constructor(
    private val repository: MovieRepositoryApi
) {
    operator fun invoke(request: SearchRequest): Flow<PagingData<MovieData>> =
        repository.getSearchStream(request)
}
