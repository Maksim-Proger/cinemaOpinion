package com.example.intro.domain.usecases

import com.example.intro.domain.repository.IntroScreensRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadAppEntryUseCase @Inject constructor(
    private val introScreensRepository: IntroScreensRepository
) {
    operator fun invoke() : Flow<Boolean?> {
        return introScreensRepository.readAppEntry()
    }
}