package com.pozmaxpav.cinemaopinion.domain.usecase.api.news

import com.pozmaxpav.cinemaopinion.domain.models.api.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi
import javax.inject.Inject

class GetMediaNewsUseCase @Inject constructor(
    private val repository: MovieRepositoryApi
) {
    suspend operator fun invoke(page:Int): NewsList {
        return repository.getMediaNews(page)
    }
}