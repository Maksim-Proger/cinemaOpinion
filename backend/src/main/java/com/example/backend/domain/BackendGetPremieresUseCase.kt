package com.example.backend.domain

import android.util.Log
import com.example.backend.PremiereItemDto
import com.example.backend.di.BackendApiProvider

class BackendGetPremieresUseCase {
    suspend operator fun invoke(year: Int, month: Int): Result<List<PremiereItemDto>> =
        runCatching {
            BackendApiProvider.api.getPremieres(year, month).items
        }.onFailure { e ->
            Log.e("BackendGetPremieres", "Failed to load premieres", e)
        }
}