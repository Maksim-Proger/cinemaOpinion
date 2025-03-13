package com.pozmaxpav.cinemaopinion.domain.usecase.movies.news

import com.pozmaxpav.cinemaopinion.domain.models.api.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import javax.inject.Inject

class GetMediaNewsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page:Int): NewsList {
        return repository.getMediaNews(page)
    }
}