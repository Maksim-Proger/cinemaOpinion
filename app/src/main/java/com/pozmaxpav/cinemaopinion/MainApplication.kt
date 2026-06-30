package com.pozmaxpav.cinemaopinion

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import com.example.backend.BackendApiProvider
import dagger.hilt.android.HiltAndroidApp
import ru.rustore.sdk.pushclient.RuStorePushClient

@HiltAndroidApp
class MainApplication: Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .okHttpClient(BackendApiProvider.imageHttpClient)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .build()
            }
            .respectCacheHeaders(false)
            .crossfade(true)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

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
        val channelId = "default_push_channel"
        val channelName = "Push Notifications"

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
