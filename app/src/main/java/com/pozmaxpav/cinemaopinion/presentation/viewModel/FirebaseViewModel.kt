package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.modelfirebase.FilmDomain
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

    private val _movies = MutableStateFlow<List<FilmDomain>>(emptyList())
    val movies: StateFlow<List<FilmDomain>> = _movies

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

    fun saveMovie(title: String) {
        viewModelScope.launch {
            try {
                saveMovieUseCase(title)
                _errorMessage.value = null // Зачем это нам?
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun removeMovie(title: String) {
        viewModelScope.launch {
            try {
                removeMovieUseCase(title)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}