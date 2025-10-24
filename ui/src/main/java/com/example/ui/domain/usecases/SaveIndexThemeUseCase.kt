package com.example.ui.domain.usecases

import com.example.ui.domain.repositories.ThemeRepository
import javax.inject.Inject


class SaveIndexThemeUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    operator fun invoke(index: Int) {
        repository.saveIndexTheme(index)
    }
}