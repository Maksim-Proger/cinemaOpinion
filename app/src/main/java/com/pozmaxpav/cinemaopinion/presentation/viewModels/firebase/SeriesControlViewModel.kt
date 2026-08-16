package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.models.voice.VoiceCommand
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.DeleteEntryUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.GetListEntriesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.AddNewEntryUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.ObserveListEntriesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.seriescontrol.UpdateEntryUseCase
import com.pozmaxpav.cinemaopinion.domain.voice.SeriesTitleMatcher
import com.pozmaxpav.cinemaopinion.domain.voice.VoiceCommandParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed interface VoiceCommandState {
    data object Idle : VoiceCommandState
    data class AwaitingConfirmation(val updated: DomainSeriesControlModel) : VoiceCommandState
    data object NotRecognized : VoiceCommandState
    data class TitleNotFound(val title: String) : VoiceCommandState
    data class NoNumericSeasons(val title: String) : VoiceCommandState
}

@HiltViewModel
class SeriesControlViewModel @Inject constructor(
    private val addNewEntryUseCase: AddNewEntryUseCase,
    private val getListEntriesUseCase: GetListEntriesUseCase,
    private val observeListEntriesUseCase: ObserveListEntriesUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase,
    private val updateMovieUseCase: UpdateEntryUseCase,
    private val voiceCommandParser: VoiceCommandParser
) : ViewModel() {

    private val _listMovies = MutableStateFlow<List<DomainSeriesControlModel>>(emptyList())
    val listMovies: StateFlow<List<DomainSeriesControlModel>> = _listMovies

    private val _voiceCommandState = MutableStateFlow<VoiceCommandState>(VoiceCommandState.Idle)
    val voiceCommandState = _voiceCommandState.asStateFlow()

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
            try {
                observeListEntriesUseCase(userId) { onEntriesUpdated ->
                    _listMovies.value = onEntriesUpdated
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
    fun updateMovie(
        userId: String,
        entryId: String,
        title: String,
        noSeasons: Boolean,
        partname: String,
        season: Int,
        series: Int
    ) {
        viewModelScope.launch {
            try {
                val selectedEntry = DomainSeriesControlModel(
                    entryId,
                    title,
                    noSeasons,
                    partname,
                    season,
                    series
                )
                updateMovieUseCase(userId, entryId, selectedEntry)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun applyVoiceCommand(userId: String, spokenText: String) {
        viewModelScope.launch {
            try {
                val command = voiceCommandParser.parse(spokenText)
                if (command == null) {
                    _voiceCommandState.value = VoiceCommandState.NotRecognized
                    return@launch
                }
                val entries = getListEntriesUseCase(userId)
                val entry = SeriesTitleMatcher.findBestMatch(entries, command.title)
                if (entry == null) {
                    _voiceCommandState.value = VoiceCommandState.TitleNotFound(command.title)
                    return@launch
                }
                if (entry.noSeasons && command !is VoiceCommand.NextEpisode) {
                    _voiceCommandState.value = VoiceCommandState.NoNumericSeasons(entry.title)
                    return@launch
                }
                val updated = when (command) {
                    is VoiceCommand.SetExact -> entry.copy(
                        season = command.season,
                        series = command.series
                    )
                    is VoiceCommand.NextEpisode -> entry.copy(series = entry.series + 1)
                    is VoiceCommand.NewSeason -> entry.copy(
                        season = entry.season + 1,
                        series = 1
                    )
                }
                _voiceCommandState.value = VoiceCommandState.AwaitingConfirmation(updated)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun confirmVoiceCommand(userId: String) {
        val state = _voiceCommandState.value
        if (state !is VoiceCommandState.AwaitingConfirmation) return
        viewModelScope.launch {
            try {
                updateMovieUseCase(userId, state.updated.id, state.updated)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _voiceCommandState.value = VoiceCommandState.Idle
            }
        }
    }
    fun resetVoiceCommandState() {
        _voiceCommandState.value = VoiceCommandState.Idle
    }

    public override fun onCleared() {
        observeListEntriesUseCase.removeListener()
        super.onCleared()
    }

}