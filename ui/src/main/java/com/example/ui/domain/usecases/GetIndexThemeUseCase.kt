package com.example.ui.domain.usecases

import com.example.ui.domain.repositories.ThemeRepository
import javax.inject.Inject

class GetIndexThemeUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    operator fun invoke(): Int {
        return repository.getIndexTheme()
    }
}