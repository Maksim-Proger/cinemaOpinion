package com.pozmaxpav.cinemaopinion.presentation.viewModel.introduction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.introductoryscreens.domain.usecases.AppEntryUseCases
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionScreensViewModel @Inject constructor(
    appEntryUseCases: AppEntryUseCases
) : ViewModel() {

    private val _startDestination = MutableStateFlow(Route.AppStartNavigation.route)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            appEntryUseCases.readAppEntry().collect { shouldStartFromHomeScreen ->
                _startDestination.value = if (shouldStartFromHomeScreen) {
                    Route.MainScreen.route
                } else {
                    Route.AppStartNavigation.route
                }
                delay(300)
                _isLoading.value = false
            }
        }
    }
}

