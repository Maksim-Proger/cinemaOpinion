package com.pozmaxpav.cinemaopinion.utilities.notification

interface DeviceDataCreatedListener {
    suspend fun onDataDeviceCreated(userId: String, pushToken: String, deviceId: String)
}
