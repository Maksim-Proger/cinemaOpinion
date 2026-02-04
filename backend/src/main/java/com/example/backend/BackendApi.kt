package com.example.backend
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendApi {

    @POST("events/change-created")
    suspend fun notifyChangeCreated(
        @Body body: ChangeCreatedRequest
    )

    @POST("devices/register")
    suspend fun registerDevice(
        @Body body: RegisterDeviceRequest
    )

}

