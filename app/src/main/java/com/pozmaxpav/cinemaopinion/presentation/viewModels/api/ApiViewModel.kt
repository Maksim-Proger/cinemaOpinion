package com.pozmaxpav.cinemaopinion.presentation.viewModels.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pozmaxpav.cinemaopinion.domain.models.api.information.Information
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.SearchRequest
import com.pozmaxpav.cinemaopinion.domain.models.system.CompositeRequest
import com.pozmaxpav.cinemaopinion.domain.usecase.api.information.GetMovieInformationUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetPremiereMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchMovieByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchMoviesPagingUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetTopMoviesPagingUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetTopMoviesUseCase
import com.example.core.utils.state.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(
    private val getPremiereMoviesUseCase: GetPremiereMoviesUseCase,
    private val getTopMoviesUseCase: GetTopMoviesUseCase,
    private val getTopMoviesPagingUseCase: GetTopMoviesPagingUseCase,
    private val getSearchMoviesPagingUseCase: GetSearchMoviesPagingUseCase,
    private val getMovieInformationUseCase: GetMovieInformationUseCase,
    private val getSearchMovieByIdUseCase: GetSearchMovieByIdUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Success)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _premiereMovies = MutableStateFlow<List<MovieData.Movie>>(emptyList())
    val premiersMovies: StateFlow<List<MovieData.Movie>> = _premiereMovies.asStateFlow()

    private val _topListMovies = MutableStateFlow<List<MovieData.MovieTop>>(emptyList())
    val topListMovies: StateFlow<List<MovieData.MovieTop>> = _topListMovies.asStateFlow()

    var isInitialized = false
        private set

    val topMoviesPaging: Flow<PagingData<MovieData>> =
        getTopMoviesPagingUseCase().cachedIn(viewModelScope)

    private val _searchRequest = MutableStateFlow<SearchRequest?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchMoviesPaging: Flow<PagingData<MovieData>> = _searchRequest
        .filterNotNull()
        .flatMapLatest { getSearchMoviesPagingUseCase(it) }
        .cachedIn(viewModelScope)

    private val _movieInfo = MutableStateFlow<Information?>(null)
    val movieInfo: StateFlow<Information?> get() = _movieInfo.asStateFlow()

    private val _detailedInfo = MutableStateFlow<MovieData.MovieSearch?>(null)
    val detailedInfo = _detailedInfo.asStateFlow()

    fun searchByKeyword(keyword: String) {
        _searchRequest.value = SearchRequest.Keyword(keyword)
    }

    fun searchByFilters(request: CompositeRequest) {
        _searchRequest.value = SearchRequest.Filters(request)
    }

    fun getSearchMovieById(id: Int) {
        viewModelScope.launch {
            try {
                val info = getSearchMovieByIdUseCase(id)
                _detailedInfo.value = info
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getInformationMovie(movieId: Int) {
        viewModelScope.launch {
            try {
                val info = getMovieInformationUseCase(movieId)
                _movieInfo.value = info
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun fetchPremiersMovies(year: Int, month: String) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading
            try {
                _premiereMovies.value = getPremiereMoviesUseCase(year, month).items
                _loadingState.value = LoadingState.Success
                isInitialized = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun fetchTopListMovies(page: Int = 1) {
        viewModelScope.launch {
            try {
                _topListMovies.value = getTopMoviesUseCase(page).films
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
