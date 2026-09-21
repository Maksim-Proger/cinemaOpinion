package com.example.backend.domain

import android.util.Log
import com.example.backend.di.BackendApiProvider
import com.example.backend.RegisterDeviceRequest

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
