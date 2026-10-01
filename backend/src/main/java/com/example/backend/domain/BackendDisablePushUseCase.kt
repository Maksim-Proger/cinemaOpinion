package com.example.backend.domain

import android.util.Log
import com.example.backend.di.BackendApiProvider
import com.example.backend.DisablePushRequest

class BackendDisablePushUseCase {
    suspend operator fun invoke(userId: String, deviceId: String): Result<Unit> =
        runCatching {
            BackendApiProvider.api.disablePush(
                DisablePushRequest(
                    userId = userId,
                    deviceId = deviceId
                )
            )
        }.onFailure { e ->
            Log.e("BackendDisablePush", "Failed to disable push for device $deviceId", e)
        }
}
