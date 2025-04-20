package com.pozmaxpav.cinemaopinion.presentation.viewModel.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.GetIndexThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.GetModeActivationSystemThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.GetModeApplicationThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.SaveIndexThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.SaveModeActivationSystemThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.SaveModeApplicationThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val saveModeApplicationThemeUseCase: SaveModeApplicationThemeUseCase,
    private val getModeApplicationThemeUseCase: GetModeApplicationThemeUseCase,
    private val saveModeActivationSystemThemeUseCase: SaveModeActivationSystemThemeUseCase,
    private val getModeActivationSystemThemeUseCase: GetModeActivationSystemThemeUseCase,
    private val saveIndexThemeUseCase: SaveIndexThemeUseCase,
    private val getIndexThemeUseCase: GetIndexThemeUseCase
) : ViewModel() {

    private val _isDarkThemeActive = MutableStateFlow(
        runCatching { getModeApplicationThemeUseCase() }.getOrDefault(false)
    )
    val isDarkThemeActive: StateFlow<Boolean> = _isDarkThemeActive.asStateFlow()

    private val _isSystemThemeActive = MutableStateFlow(
        runCatching { getModeActivationSystemThemeUseCase() }.getOrDefault(false)
    )
    val isSystemThemeActive: StateFlow<Boolean> = _isSystemThemeActive.asStateFlow()

    private val _indexSelectedTheme = MutableStateFlow(0)
    val indexSelectedTheme = _indexSelectedTheme.asStateFlow()

    init {
        getIndexTheme()
    }

    fun saveIndexTheme(index: Int) {
        viewModelScope.launch {
            try {
                saveIndexThemeUseCase(index)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getIndexTheme() {
        viewModelScope.launch {
            try {
                val index = getIndexThemeUseCase()
                _indexSelectedTheme.value = index
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changeModeTheme(isThemeMode: Boolean) {
        _isDarkThemeActive.value = isThemeMode
        saveModeApplicationThemeUseCase(isThemeMode)
    }

    fun changeStatusUsingSystemTheme(isSystemThemeMode: Boolean) {
        _isSystemThemeActive.value = isSystemThemeMode
        saveModeActivationSystemThemeUseCase(isSystemThemeMode)
    }

}