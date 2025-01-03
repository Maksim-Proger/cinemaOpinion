package com.pozmaxpav.cinemaopinion.presentation.viewModel

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.information.Information
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetPremiereMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchFilmsByFiltersUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchMovieByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchMovies2UseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetTopMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.information.GetMovieInformationUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.news.GetMediaNewsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetAppVersionUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetResultCheckingUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveAppVersionUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveResultCheckingUseCase
import com.pozmaxpav.cinemaopinion.utilits.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPremiereMoviesUseCase: GetPremiereMoviesUseCase,
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase,
    private val getSearchMovies2UseCase: GetSearchMovies2UseCase,
    private val getTopMoviesUseCase: GetTopMoviesUseCase,
    private val getSearchFilmsByFiltersUseCase: GetSearchFilmsByFiltersUseCase,
    private val getMovieInformationUseCase: GetMovieInformationUseCase,
    private val getMediaNewsUseCase: GetMediaNewsUseCase,
    private val getSearchMovieByIdUseCase: GetSearchMovieByIdUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val saveAppVersionUseCase: SaveAppVersionUseCase,
    private val getResultCheckingUseCase: GetResultCheckingUseCase,
    private val saveResultCheckingUseCase: SaveResultCheckingUseCase
) : ViewModel() {

    private val _versionApp = MutableStateFlow("Unknown")
    val versionApp = _versionApp.asStateFlow()

    private val _resultChecking = MutableStateFlow(false)
    val resultChecking = _resultChecking.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Success)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _premiereMovies = MutableStateFlow<MovieList?>(null)
    val premiersMovies: StateFlow<MovieList?> get() = _premiereMovies.asStateFlow()

    private val _topListMovies = MutableStateFlow<MovieTopList?>(null)
    val topListMovies: StateFlow<MovieTopList?> get() = _topListMovies.asStateFlow()



    private val _searchMovies = MutableStateFlow<MovieSearchList?>(null)
    val searchMovies = _searchMovies.asStateFlow()
    private val _searchMovies2 = MutableStateFlow<MovieSearchList2?>(null)
    val searchMovies2 = _searchMovies2.asStateFlow()



    private val _searchMovieById = MutableStateFlow<MovieSearch?>(null)
    val searchMovieById: StateFlow<MovieSearch?> get() = _searchMovieById.asStateFlow()

    private val _informationMovie = MutableStateFlow<Information?>(null)
    val informationMovie: StateFlow<Information?> get() = _informationMovie.asStateFlow()

    private val _mediaNews = MutableStateFlow<NewsList?>(null)
    val mediaNews: StateFlow<NewsList?> get() = _mediaNews.asStateFlow()

    init {
        getAppVersion()
        getResultChecking()
    }

    fun resetResultChecking() {
        _resultChecking.value = true
    }

    fun saveAppVersion(version: String) {
        viewModelScope.launch {
            try {
                saveAppVersionUseCase(version)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAppVersion() {
        viewModelScope.launch {
            try {
                val version = getAppVersionUseCase() ?: "Unknown"
                _versionApp.value = version
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveResultChecking(resultChecking: Boolean) {
        viewModelScope.launch {
            saveResultCheckingUseCase(resultChecking)
        }
    }

    fun getResultChecking() {
        viewModelScope.launch {
            try {
                val result = getResultCheckingUseCase()
                _resultChecking.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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

    fun getSearchMovieById(id: Int) {
        viewModelScope.launch {
            try {
                val searchMovie = getSearchMovieByIdUseCase(id)
                _searchMovieById.value = searchMovie
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    } // TODO: Работает, но пока не задействован в приложении

    fun fetchPremiersMovies(year: Int, month: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val movies = getPremiereMoviesUseCase(year, month)
                _premiereMovies.value = movies
                delay(500)
                _state.value = State.Success
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
    
    fun searchFilmsByFilters(
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
//                _searchMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}








