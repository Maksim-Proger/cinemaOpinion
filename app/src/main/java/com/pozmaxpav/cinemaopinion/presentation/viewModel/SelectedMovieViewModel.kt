package com.pozmaxpav.cinemaopinion.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.GetFilmByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.GetListSelectedFilmsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.InsertFilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedMovieViewModel @Inject constructor(
    private val getListSelectedFilmsUseCase: GetListSelectedFilmsUseCase,
    private val insertFilmUseCase: InsertFilmUseCase,
    private val getFilmById: GetFilmByIdUseCase
) : ViewModel() {

    private val _selectedMovies = MutableStateFlow<List<SelectedMovie>>(emptyList())
    val selectedMovies: StateFlow<List<SelectedMovie>> = _selectedMovies

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    fun fitchListSelectedMovies() {
        viewModelScope.launch {
            val movies = getListSelectedFilmsUseCase()
            _selectedMovies.value = movies
        }
    }

    fun addSelectedMovie(selectedMovie: SelectedMovie) {
        viewModelScope.launch {
            val existMovie = getFilmById(selectedMovie.id)
            if (existMovie == null) {
                insertFilmUseCase.invoke(selectedMovie)
            } else {
                _status.value = "error"
            }
        }
    }




}