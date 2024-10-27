package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.SeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCDeleteMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCGetListMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCGetMovieByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCInsertUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCUpdateMovieUseCase
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
    private val insertUseCase: SCInsertUseCase,
    private val updateMovieUseCase: SCUpdateMovieUseCase
) : ViewModel() {

    private val _listMovies = MutableStateFlow<List<SeriesControlModel>>(emptyList())
    val listMovies: StateFlow<List<SeriesControlModel>> = _listMovies

    init {
        getListMovies()
    }

    private fun getListMovies() {
        viewModelScope.launch {
            try {
                getListMoviesUseCase().collect { movies ->
                    _listMovies.value = movies
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertMovie(title: String, season: Int = 0, series: Int = 0) {
        viewModelScope.launch {
            try {
                val movie = SeriesControlModel(
                    id = 0, // Это значение будет игнорироваться, так как id автоинкрементный
                    title = title,
                    season = season,
                    series = series
                )
                insertUseCase(movie)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateMovie(id: Int, season: Int, series: Int) {
        viewModelScope.launch {
            try {
                val movie = getMovieByIdUseCase(id)
                val updatedMovie = movie!!.copy(season = season, series = series) // Обновляем нужные параметры
                updateMovieUseCase(updatedMovie)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteMovie(id: Int) {
        viewModelScope.launch {
            try {
                val movie = getMovieByIdUseCase(id)
                deleteMovieUseCase(movie!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}