package com.example.intro.domain.repository

import kotlinx.coroutines.flow.Flow

interface IntroScreensRepository {
    suspend fun saveAppEntry()
    fun readAppEntry(): Flow<Boolean?>
}