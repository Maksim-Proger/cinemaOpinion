package com.pozmaxpav.cinemaopinion.utilities.notification

import com.example.backend.BackendRegisterDeviceUseCase

class BackendDeviceDataCreatedListener(
    private val backendRegisterDeviceUseCase: BackendRegisterDeviceUseCase
) : DeviceDataCreatedListener {
    override suspend fun onDataDeviceCreated(userId: String, pushToken: String, deviceId: String) {
        backendRegisterDeviceUseCase.invoke(userId, pushToken, deviceId)
    }
}
