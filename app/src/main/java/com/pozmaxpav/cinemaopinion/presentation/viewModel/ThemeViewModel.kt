package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.GetModeActivationSystemTheme
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.GetModeApplicationThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.SaveModeActivationSystemThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.SaveModeApplicationThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val saveModeApplicationThemeUseCase: SaveModeApplicationThemeUseCase,
    private val getModeApplicationThemeUseCase: GetModeApplicationThemeUseCase, // TODO: Почему val горит серым
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

    fun getStatusUsingSystemTheme(): Boolean { // TODO: Почему мы не используем этот метод
        return getModeActivationSystemTheme.execute()
    }
}