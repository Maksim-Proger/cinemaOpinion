package com.example.introductoryscreens.domain.usecases

import com.example.introductoryscreens.domain.LocalUserManager

class SaveAppEntry(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke() {
        localUserManager.saveAppEntry()
    }
}