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

