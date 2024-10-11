package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.SeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCDeleteMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCGetListMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCGetMovieByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCInsertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesControlViewModel @Inject constructor(
    private val deleteMovieUseCase: SCDeleteMovieUseCase,
    private val getListMoviesUseCase: SCGetListMoviesUseCase,
    private val getMovieByIdUseCase: SCGetMovieByIdUseCase,
    private val insertUseCase: SCInsertUseCase
) : ViewModel() {

    private val _listMovies = MutableStateFlow<List<SeriesControlModel>>(emptyList())
    val listMovies: StateFlow<List<SeriesControlModel>> = _listMovies

    init {
        getListMovies()
    }

    private fun getListMovies() {
        viewModelScope.launch {
            getListMoviesUseCase().collect { movies ->
                _listMovies.value = movies
            }
        }
    }

    fun insertMovies(title: String, season: Int = 0, series: Int = 0) {
        viewModelScope.launch {
            val movie = SeriesControlModel(
                id = 0, // Это значение будет игнорироваться, так как id автоинкрементный
                title = title,
                season = season,
                series = series
            )
            insertUseCase(movie)
        }
    }

}