package com.example.intro.presentation.introscreens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intro.domain.usecases.ReadAppEntryUseCase
import com.example.intro.domain.usecases.SaveAppEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val readAppEntryUseCase: ReadAppEntryUseCase,
    private val saveAppEntryUseCase: SaveAppEntryUseCase
) : ViewModel() {

    private val _hasUserEnteredApp = MutableStateFlow<Boolean?>(null)
    val hasUserEnteredApp: StateFlow<Boolean?> = _hasUserEnteredApp

    init {
        viewModelScope.launch {
            readAppEntryUseCase().collect { value ->
                _hasUserEnteredApp.value = value
            }
        }
    }

    fun saveAppEntry() {
        viewModelScope.launch {
            saveAppEntryUseCase()
        }
    }

}