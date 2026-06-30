package com.example.backend

import com.google.gson.annotations.SerializedName

data class RegisterDeviceRequest(
    @SerializedName("userId") val userId: String, // Добавлено обязательное поле
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("pushToken") val pushToken: String,
    @SerializedName("platform") val platform: String = "android" // Сервер имеет дефолт, но лучше передать явно
)

data class ChangeCreatedRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("changeId") val changeId: String
)

data class DisablePushRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("deviceId") val deviceId: String
)

data class UploadAvatarResponse(
    @SerializedName("status") val status: String,
    @SerializedName("avatarId") val avatarId: String
)

