package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.GetListSelectedFilmsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.InsertFilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedMovieViewModel @Inject constructor(
    private val getListSelectedFilmsUseCase: GetListSelectedFilmsUseCase,
    private val insertFilmUseCase: InsertFilmUseCase
) : ViewModel() {

    private val _selectedMovies = MutableStateFlow<List<SelectedMovie>>(emptyList())
    val selectedMovies: StateFlow<List<SelectedMovie>> = _selectedMovies

    fun fitchListSelectedMovies() {
        viewModelScope.launch {
            val movies = getListSelectedFilmsUseCase()
            _selectedMovies.value = movies
        }
    }

    fun addSelectedMovie(selectedMovie: SelectedMovie) {
        viewModelScope.launch {
            insertFilmUseCase.invoke(selectedMovie)
        }
    }




}