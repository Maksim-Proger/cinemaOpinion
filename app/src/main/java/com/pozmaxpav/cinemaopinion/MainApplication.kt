package com.pozmaxpav.cinemaopinion

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import ru.rustore.sdk.pushclient.RuStorePushClient

@HiltAndroidApp
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Создаем канал уведомлений (Android 8+)
        createNotificationChannel()

        // Инициализация RuStore Push SDK
        RuStorePushClient.init(
            application = this,
            projectId = "Ehw2k7VF29fSe5pbUk8h4w5P63jr3kIB"
        )

        // Получаем токен и логируем его
        RuStorePushClient.getToken()
            .addOnSuccessListener { token ->
                Log.d("RuStorePush", "Push Token получен: $token")
            }
            .addOnFailureListener { throwable ->
                Log.e("RuStorePush", "Ошибка при получении токена", throwable)
            }

        Log.d("RuStorePush", "RuStore Push SDK инициализирован")
    }

    private fun createNotificationChannel() {
        // ID и имя канала
        val channelId = "default_push_channel"
        val channelName = "Push Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for app push notifications"
            }

            val notificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}
