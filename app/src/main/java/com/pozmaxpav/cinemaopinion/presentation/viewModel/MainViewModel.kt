package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.MovieList
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
): ViewModel() {
    private val _premiereMovies = MutableStateFlow<MovieList?>(null)
    val premiersMovies: StateFlow<MovieList?> get() = _premiereMovies.asStateFlow()

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
}