package com.pozmaxpav.cinemaopinion.utilities.notification

import com.example.backend.BackendDisablePushUseCase

class BackendDeviceDataDisabledListener(
    private val backendDisablePushUseCase: BackendDisablePushUseCase
) : DeviceDataDisabledListener {
    override suspend fun onDataDeviceDisabled(userId: String, deviceId: String) {
        backendDisablePushUseCase.invoke(userId, deviceId).onFailure {}
    }
}
