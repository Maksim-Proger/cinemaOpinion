package com.pozmaxpav.cinemaopinion.presentation.viewModel.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.api.information.Information
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.api.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.usecase.api.information.GetMovieInformationUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetPremiereMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchFilmsByFiltersUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchMovieByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchMovies2UseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetTopMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.news.GetMediaNewsUseCase
import com.pozmaxpav.cinemaopinion.utilits.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(
    private val getPremiereMoviesUseCase: GetPremiereMoviesUseCase,
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase,
    private val getSearchMovies2UseCase: GetSearchMovies2UseCase,
    private val getTopMoviesUseCase: GetTopMoviesUseCase,
    private val getSearchFilmsByFiltersUseCase: GetSearchFilmsByFiltersUseCase,
    private val getMovieInformationUseCase: GetMovieInformationUseCase,
    private val getMediaNewsUseCase: GetMediaNewsUseCase,
    private val getSearchMovieByIdUseCase: GetSearchMovieByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Success)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _premiereMovies = MutableStateFlow<MovieList?>(null)
    val premiersMovies: StateFlow<MovieList?> get() = _premiereMovies.asStateFlow()

    private val _topListMovies = MutableStateFlow<MovieTopList?>(null)
    val topListMovies: StateFlow<MovieTopList?> get() = _topListMovies.asStateFlow()

    var isInitialized = false

    private val _searchMovies = MutableStateFlow<MovieSearchList?>(null)
    val searchMovies = _searchMovies.asStateFlow()
    private val _searchMovies2 = MutableStateFlow<MovieSearchList2?>(null)
    val searchMovies2 = _searchMovies2.asStateFlow()

    private val _informationMovie = MutableStateFlow<Information?>(null)
    val informationMovie: StateFlow<Information?> get() = _informationMovie.asStateFlow()

    private val _mediaNews = MutableStateFlow<NewsList?>(null)
    val mediaNews: StateFlow<NewsList?> get() = _mediaNews.asStateFlow()

    fun getMediaNews(page:Int) {
        viewModelScope.launch {
            try {
                val news = getMediaNewsUseCase(page)
                _mediaNews.value = news
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getInformationMovie(movieId: Int) {
        viewModelScope.launch {
            try {
                val info = getMovieInformationUseCase(movieId)
                _informationMovie.value = info
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun fetchPremiersMovies(year: Int, month: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val movies = getPremiereMoviesUseCase(year, month)
                _premiereMovies.value = movies
                delay(500)
                _state.value = State.Success
                isInitialized = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun fetchTopListMovies(page: Int) {
        viewModelScope.launch {
            try {
                val movies = getTopMoviesUseCase(page)
                _topListMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun fetchSearchMovies(keyword: String, page: Int) {
        viewModelScope.launch {
            try {
                val movies = getSearchMoviesUseCase(keyword, page)
                _searchMovies.value = movies

                if (movies.items.isEmpty()) {
                    val fallbackMovies  = getSearchMovies2UseCase(keyword, page)
                    _searchMovies2.value = fallbackMovies
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun searchFilmsByFilters( // TODO: Дописать возможность поиска из второй базы!
        type: String?,
        keyword: String?,
        countries: Int?,
        genres: Int?,
        ratingFrom: Int?,
        yearFrom: Int?,
        yearTo: Int?,
        page: Int
    ) {
        viewModelScope.launch {
            try {
                val movies = getSearchFilmsByFiltersUseCase(
                    type, keyword, countries, genres, ratingFrom, yearFrom, yearTo, page
                )
                _searchMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}