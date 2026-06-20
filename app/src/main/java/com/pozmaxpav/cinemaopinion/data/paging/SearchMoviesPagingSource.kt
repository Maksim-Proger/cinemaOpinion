package com.pozmaxpav.cinemaopinion.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.SearchRequest
import com.pozmaxpav.cinemaopinion.domain.models.system.CompositeRequest
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi

class SearchMoviesPagingSource(
    private val repository: MovieRepositoryApi,
    private val request: SearchRequest
) : PagingSource<Int, MovieData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieData> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val (movies, totalPages) = when (request) {
                is SearchRequest.Keyword -> loadByKeyword(request.keyword, page)
                is SearchRequest.Filters -> loadByFilters(request.request, page)
            }
            LoadResult.Page(
                data = movies,
                prevKey = if (page == FIRST_PAGE) null else page - 1,
                nextKey = if (movies.isEmpty() || page >= totalPages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun loadByKeyword(keyword: String, page: Int): Pair<List<MovieData>, Int> {
        val primary = repository.getSearchMovies(keyword, page)
        return if (primary.items.isNotEmpty()) {
            primary.items to primary.totalPages
        } else {
            val fallback = repository.getSearchMovies2(keyword, page)
            fallback.films to fallback.pagesCount
        }
    }

    private suspend fun loadByFilters(request: CompositeRequest, page: Int): Pair<List<MovieData>, Int> {
        val result = repository.getSearchFilmsByFilters(
            request.type,
            request.keyword,
            request.countries,
            request.genres,
            request.ratingFrom,
            request.yearFrom,
            request.yearTo,
            page
        )
        return result.items to result.totalPages
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
