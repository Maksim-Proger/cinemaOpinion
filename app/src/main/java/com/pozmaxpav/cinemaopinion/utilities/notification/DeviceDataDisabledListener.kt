package com.pozmaxpav.cinemaopinion.utilities.notification

interface DeviceDataDisabledListener {
    suspend fun onDataDeviceDisabled(userId: String, deviceId: String)
}
