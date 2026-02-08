package com.pozmaxpav.cinemaopinion.utilities.notification

import android.app.NotificationManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetUserIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SavePushTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rustore.sdk.pushclient.messaging.model.RemoteMessage
import ru.rustore.sdk.pushclient.messaging.service.RuStoreMessagingService
import javax.inject.Inject
import com.pozmaxpav.cinemaopinion.R


@AndroidEntryPoint
class MyRuStoreMessagingService : RuStoreMessagingService() {
    @Inject
    lateinit var savePushTokenUseCase: SavePushTokenUseCase

    @Inject
    lateinit var createdListener: DeviceDataCreatedListener

    @Inject
    lateinit var getUserId: GetUserIdUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // 1. Сохраняем pushToken в Shared Preferences (всегда новый)
        savePushTokenUseCase(token)

        // 2. Пробуем получить текущего пользователя и deviceId
        val userId = getUserId()
        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        if (userId != null && userId != "Unknown") {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Отправляем pushToken и deviceId в модуль backend для дальнейшей отправки на сервер
                    createdListener.onDataDeviceCreated(userId, token, deviceId)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_push_channel"

        // Правильно берём данные
        val title = message.data["title"] ?: message.notification?.title ?: "Push Title"

        val body = message.data["body"] ?: message.notification?.body ?: "Push Body"

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.notification?.body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(message.hashCode(), notification)
    }
}
