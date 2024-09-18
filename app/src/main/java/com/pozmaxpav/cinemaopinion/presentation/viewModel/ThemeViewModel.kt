package com.pozmaxpav.cinemaopinion.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pozmaxpav.cinemaopinion.domain.usecase.GetModeActivationSystemTheme
import com.pozmaxpav.cinemaopinion.domain.usecase.GetModeApplicationThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.SaveModeActivationSystemThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.SaveModeApplicationThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val saveModeApplicationThemeUseCase: SaveModeApplicationThemeUseCase,
    private val getModeApplicationThemeUseCase: GetModeApplicationThemeUseCase,
    private val saveModeActivationSystemThemeUseCase: SaveModeActivationSystemThemeUseCase,
    private val getModeActivationSystemTheme: GetModeActivationSystemTheme
) : ViewModel() {

    private val _isDarkThemeActive = MutableStateFlow(getModeApplicationThemeUseCase.execute())
    val isDarkThemeActive: StateFlow<Boolean> = _isDarkThemeActive.asStateFlow()

    private val _isSystemThemeActive = MutableStateFlow(getModeActivationSystemTheme.execute())
    val isSystemThemeActive: StateFlow<Boolean> = _isSystemThemeActive.asStateFlow()

    fun changeModeTheme(isThemeMode: Boolean) {
        _isDarkThemeActive.value = isThemeMode
        saveModeApplicationThemeUseCase.execute(isThemeMode)
    }

    fun changeStatusUsingSystemTheme(isSystemThemeMode: Boolean) {
        _isSystemThemeActive.value = isSystemThemeMode
        saveModeActivationSystemThemeUseCase.execute(isSystemThemeMode)
    }

    fun getStatusUsingSystemTheme(): Boolean {
        return getModeActivationSystemTheme.execute()
    }
}