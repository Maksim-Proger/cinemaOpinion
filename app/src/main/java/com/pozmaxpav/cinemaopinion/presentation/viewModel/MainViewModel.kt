package com.pozmaxpav.cinemaopinion.presentation.viewModel


// TODO: Почему тут нам надо вызывать execute?
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieData.MovieSearch
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.information.Information
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetPremiereMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchFilmsByFiltersUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchMovieByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetTopMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.information.GetMovieInformationUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.news.GetMediaNewsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetStateAppDescriptionFlagUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetStateSeasonalFlagUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveStateAppDescriptionFlagUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveStateSeasonalFlagUseCase
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
    private val getTopMoviesUseCase: GetTopMoviesUseCase,
    private val getSearchFilmsByFiltersUseCase: GetSearchFilmsByFiltersUseCase,
    private val getMovieInformationUseCase: GetMovieInformationUseCase,
    private val getMediaNewsUseCase: GetMediaNewsUseCase,
    private val getSearchMovieByIdUseCase: GetSearchMovieByIdUseCase,
    private val saveStateSeasonalFlagUseCase: SaveStateSeasonalFlagUseCase,
    private val getStateSeasonalFlagUseCase: GetStateSeasonalFlagUseCase,
    private val getStateAppDescriptionFlagUseCase: GetStateAppDescriptionFlagUseCase,
    private val saveStateAppDescriptionFlagUseCase: SaveStateAppDescriptionFlagUseCase
) : ViewModel() {

    private val _appDescriptionFlag = MutableStateFlow(false)
    val appDescriptionFlag = _appDescriptionFlag.asStateFlow()

    private val _seasonalFlagForAlertDialog = MutableStateFlow(false)
    val seasonalFlagForAlertDialog: StateFlow<Boolean> = _seasonalFlagForAlertDialog.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Success)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _premiereMovies = MutableStateFlow<MovieList?>(null)
    val premiersMovies: StateFlow<MovieList?> get() = _premiereMovies.asStateFlow()

    private val _topListMovies = MutableStateFlow<MovieTopList?>(null)
    val topListMovies: StateFlow<MovieTopList?> get() = _topListMovies.asStateFlow()

    private val _searchMovies = MutableStateFlow<MovieSearchList?>(null)
    val searchMovies: StateFlow<MovieSearchList?> get() = _searchMovies.asStateFlow()

    private val _searchMovieById = MutableStateFlow<MovieSearch?>(null)
    val searchMovieById: StateFlow<MovieSearch?> get() = _searchMovieById.asStateFlow()

    private val _informationMovie = MutableStateFlow<Information?>(null)
    val informationMovie: StateFlow<Information?> get() = _informationMovie.asStateFlow()

    private val _mediaNews = MutableStateFlow<NewsList?>(null)
    val mediaNews: StateFlow<NewsList?> get() = _mediaNews.asStateFlow()

    fun saveStateAppDescriptionFlag(isAppDescriptionFlag: Boolean) {
        viewModelScope.launch {
            try {
                saveStateAppDescriptionFlagUseCase(isAppDescriptionFlag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getStateAppDescriptionFlag() {
        viewModelScope.launch {
            try {
                val state = getStateAppDescriptionFlagUseCase()
                _appDescriptionFlag.value = state
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveStateSeasonalFlag(isSeasonalFlag: Boolean) {
        viewModelScope.launch {
            try {
                saveStateSeasonalFlagUseCase.invoke(isSeasonalFlag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getStateSeasonalFlag() {
        viewModelScope.launch {
            try {
                val state = getStateSeasonalFlagUseCase.invoke()
                _seasonalFlagForAlertDialog.value = state
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
                delay(300) // Искусственная задержка
                _premiereMovies.value = movies
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
                _searchMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}








