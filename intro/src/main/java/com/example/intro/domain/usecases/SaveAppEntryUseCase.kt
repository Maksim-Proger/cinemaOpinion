package com.example.intro.domain.usecases

import com.example.intro.domain.repository.IntroScreensRepository
import javax.inject.Inject

class SaveAppEntryUseCase @Inject constructor(
    private val introScreensRepository: IntroScreensRepository
) {
    suspend operator fun invoke() {
        introScreensRepository.saveAppEntry()
    }
}