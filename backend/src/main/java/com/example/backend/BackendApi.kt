package com.example.backend
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface BackendApi {

    @Multipart
    @POST("avatars/upload")
    suspend fun uploadAvatar(
        @Part("userId") userId: RequestBody,
        @Part file: MultipartBody.Part
    ): UploadAvatarResponse

    @POST("events/change-created")
    suspend fun notifyChangeCreated(
        @Body body: ChangeCreatedRequest
    )

    @POST("devices/register")
    suspend fun registerDevice(
        @Body body: RegisterDeviceRequest
    )

    @POST("devices/disable")
    suspend fun disablePush(
        @Body body: DisablePushRequest
    )

}

