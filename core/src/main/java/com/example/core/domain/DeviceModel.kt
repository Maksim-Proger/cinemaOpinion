package com.example.core.domain

data class DeviceModel (
    val deviceId: String,
    val pushToken: String,
    val platform: String = "android",
    val pushEnabled: Boolean = true,
    val lastActiveAt: Long
)
