package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.RemoveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.SaveMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val saveMovieUseCase: SaveMovieUseCase,
    private val removeMovieUseCase: RemoveMovieUseCase,
    private val getMovieUseCase: GetMovieUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<SelectedMovie>>(emptyList())
    val movies: StateFlow<List<SelectedMovie>> = _movies

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun getMovies() {
        viewModelScope.launch {
            try {
                val moviesList = getMovieUseCase()
                _movies.value = moviesList
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun saveMovie(selectedMovie: SelectedMovie) {
        viewModelScope.launch {
            try {
                saveMovieUseCase(selectedMovie)
                _errorMessage.value = null // Зачем это нам?
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun removeMovie(id: Double) {
        viewModelScope.launch {
            try {
                removeMovieUseCase(id)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}