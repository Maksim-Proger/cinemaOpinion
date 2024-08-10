package com.pozmaxpav.cinemaopinion.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.PagedMovieList
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _premiereMovies = MutableStateFlow<MovieList?>(null)
    val premiersMovies: StateFlow<MovieList?> get() = _premiereMovies.asStateFlow()

    private val _topListMovies = MutableStateFlow<PagedMovieList?>(null)
    val topListMovies: StateFlow<PagedMovieList?> get() = _topListMovies.asStateFlow()

    fun fetchPremiersMovies(year: Int, month: String) {
        viewModelScope.launch {
            try {
                val movies = repository.getPremiereMovies(year, month)
                _premiereMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchTopListMovies(page: Int) {
        viewModelScope.launch {
            try {
                val movies = repository.getTopMovies(page)
                _topListMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}