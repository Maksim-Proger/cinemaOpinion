package com.example.backend

class BackendRegisterDeviceUseCase {
    suspend operator fun invoke(userId: String, pushToken: String, deviceId: String) {
        BackendApiProvider.api.registerDevice(
            RegisterDeviceRequest(
                userId = userId,
                deviceId = deviceId,
                pushToken = pushToken
            )
        )
    }
}
