package com.example.backend

import com.google.gson.annotations.SerializedName

data class RegisterDeviceRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("pushToken") val pushToken: String,
    @SerializedName("platform") val platform: String = "android"
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

data class PremieresResponse(
    @SerializedName("items") val items: List<PremiereItemDto>
)

data class PremiereItemDto(
    @SerializedName("kp_id") val kpId: Int?,
    @SerializedName("title_ru") val titleRu: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("premiere_ru") val premiereRu: String?,
    @SerializedName("genres") val genres: List<String>?,
    @SerializedName("countries") val countries: List<String>?,
    @SerializedName("poster_url") val posterUrl: String?,
    @SerializedName("poster_preview") val posterPreview: String?
)


