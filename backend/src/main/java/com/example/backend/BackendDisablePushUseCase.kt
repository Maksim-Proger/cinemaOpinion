package com.example.backend

import android.util.Log

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
