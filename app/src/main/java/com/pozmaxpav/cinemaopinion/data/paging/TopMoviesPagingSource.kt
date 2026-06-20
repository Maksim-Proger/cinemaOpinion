package com.pozmaxpav.cinemaopinion.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi

class TopMoviesPagingSource(
    private val repository: MovieRepositoryApi
) : PagingSource<Int, MovieData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieData> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val result = repository.getTopMovies(page)
            LoadResult.Page(
                data = result.films,
                prevKey = if (page == FIRST_PAGE) null else page - 1,
                nextKey = if (page >= result.pagesCount) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieData>): Int? {
        return state.anchorPosition?.let { anchor ->
            val closestPage = state.closestPageToPosition(anchor)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val FIRST_PAGE = 1
    }
}
