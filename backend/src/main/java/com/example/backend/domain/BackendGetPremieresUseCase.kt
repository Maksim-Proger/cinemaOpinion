package com.example.backend.domain

import android.util.Log
import com.example.backend.PremiereItemDto
import com.example.backend.di.BackendApiProvider
import retrofit2.HttpException


class BackendGetPremieresUseCase {
    suspend operator fun invoke(year: Int, month: Int): Result<List<PremiereItemDto>> =
        runCatching {
            try {
                BackendApiProvider.api.getPremieres(year, month).items
            } catch (e: HttpException) {
                if (e.code() == HTTP_NOT_FOUND) emptyList() else throw e
            }
        }.onFailure { e ->
            Log.e("BackendGetPremieres", "Failed to load premieres", e)
        }

    private companion object {
        const val HTTP_NOT_FOUND = 404
    }
}

