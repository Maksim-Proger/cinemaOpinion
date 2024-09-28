package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetPremiereMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetSearchMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.movies.GetTopMoviesUseCase
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
    private val getTopMoviesUseCase: GetTopMoviesUseCase
) : ViewModel() {

    private val _premiereMovies = MutableStateFlow<MovieList?>(null)
    val premiersMovies: StateFlow<MovieList?> get() = _premiereMovies.asStateFlow()

    private val _topListMovies = MutableStateFlow<MovieTopList?>(null)
    val topListMovies: StateFlow<MovieTopList?> get() = _topListMovies.asStateFlow()

    private val _searchMovies = MutableStateFlow<MovieSearchList?>(null)
    val searchMovies: StateFlow<MovieSearchList?> get() = _searchMovies.asStateFlow()

    fun fetchPremiersMovies(year: Int, month: String) {
        viewModelScope.launch {
            try {
                val movies = getPremiereMoviesUseCase.execute(year, month)
                _premiereMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchTopListMovies(page: Int) {
        viewModelScope.launch {
            try {
                val movies = getTopMoviesUseCase.execute(page)
                _topListMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchSearchMovies(keyword: String) {
        viewModelScope.launch {
            try {
                val movies = getSearchMoviesUseCase.execute(keyword)
                _searchMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}