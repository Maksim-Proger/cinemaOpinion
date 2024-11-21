package com.pozmaxpav.cinemaopinion.presentation.viewModel


// TODO: Почему тут нам надо вызывать execute?
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.information.Information
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetPremiereMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchFilmsByFiltersUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetTopMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.information.GetMovieInformationUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.news.GetMediaNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val getMediaNewsUseCase: GetMediaNewsUseCase
) : ViewModel() {

    private val _premiereMovies = MutableStateFlow<MovieList?>(null) // TODO: А зачем нам тут вопрос?
    val premiersMovies: StateFlow<MovieList?> get() = _premiereMovies.asStateFlow()

    private val _topListMovies = MutableStateFlow<MovieTopList?>(null)
    val topListMovies: StateFlow<MovieTopList?> get() = _topListMovies.asStateFlow()

    private val _searchMovies = MutableStateFlow<MovieSearchList?>(null)
    val searchMovies: StateFlow<MovieSearchList?> get() = _searchMovies.asStateFlow()

    private val _informationMovie = MutableStateFlow<Information?>(null)
    val informationMovie: StateFlow<Information?> get() = _informationMovie.asStateFlow()

    private val _mediaNews = MutableStateFlow<NewsList?>(null)
    val mediaNews: StateFlow<NewsList?> get() = _mediaNews.asStateFlow()

    // region Это можно использовать для анимации загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    //endregion

    fun getMediaNews(page:Int) {
        viewModelScope.launch {

            // region Это можно использовать для анимации загрузки
            if (_isLoading.value) return@launch // Если уже идет загрузка, пропускаем запрос
            _isLoading.value = true
            //endregion

            try {
                val news = getMediaNewsUseCase(page)
                _mediaNews.value = news
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // region Это можно использовать для анимации загрузки
            finally {
                _isLoading.value = false
            }
            //endregion
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
            try {
                val movies = getPremiereMoviesUseCase(year, month)
                _premiereMovies.value = movies
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








