package com.example.backend

import android.util.Log

class BackendRegisterDeviceUseCase {
    suspend operator fun invoke(userId: String, pushToken: String, deviceId: String): Result<Unit> =
        runCatching {
            BackendApiProvider.api.registerDevice(
                RegisterDeviceRequest(
                    userId = userId,
                    deviceId = deviceId,
                    pushToken = pushToken
                )
            )
        }.onFailure { e ->
            Log.e("BackendRegister", "Failed to register device", e)
        }
}
