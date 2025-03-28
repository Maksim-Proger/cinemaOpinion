package com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.DeleteEntryUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.GetListEntriesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.AddNewEntryUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.ObserveListEntriesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.UpdateEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SeriesControlViewModel @Inject constructor(
    private val addNewEntryUseCase: AddNewEntryUseCase,
    private val getListEntriesUseCase: GetListEntriesUseCase,
    private val observeListEntriesUseCase: ObserveListEntriesUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase,
    private val updateMovieUseCase: UpdateEntryUseCase
) : ViewModel() {

    private val _listMovies = MutableStateFlow<List<DomainSeriesControlModel>>(emptyList())
    val listMovies = _listMovies.asStateFlow()

    fun getListEntries(userId: String) {
        viewModelScope.launch {
            try {
                val listEntries = getListEntriesUseCase(userId)
                _listMovies.value = listEntries
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun observeListEntries(userId: String) {
        viewModelScope.launch {
            observeListEntriesUseCase(userId) { onEntriesUpdated ->
                _listMovies.value = onEntriesUpdated
            }
        }
    }
    fun addNewEntry(userId: String, title: String, season: Int = 0, series: Int = 0) {
        viewModelScope.launch {
            try {
                val entry = DomainSeriesControlModel(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    season = season,
                    series = series
                )
                addNewEntryUseCase(userId, entry)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun deleteMovie(userId: String, entryId: String) {
        viewModelScope.launch {
            try {
                deleteEntryUseCase(userId, entryId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun updateMovie(userId: String, entryId: String, title: String, season: Int, series: Int) {
        viewModelScope.launch {
            try {
                val selectedEntry = DomainSeriesControlModel(
                    entryId,
                    title,
                    season,
                    series
                )
                updateMovieUseCase(userId, entryId, selectedEntry)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    public override fun onCleared() {
        observeListEntriesUseCase.removeListener()
        super.onCleared()
    }

}