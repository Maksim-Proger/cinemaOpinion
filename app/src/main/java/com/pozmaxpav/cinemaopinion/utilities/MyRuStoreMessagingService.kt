package com.pozmaxpav.cinemaopinion.utilities

import android.util.Log
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SavePushTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rustore.sdk.pushclient.messaging.service.RuStoreMessagingService
import javax.inject.Inject

@AndroidEntryPoint
class MyRuStoreMessagingService : RuStoreMessagingService() {

    @Inject
    lateinit var savePushTokenUseCase: SavePushTokenUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("RuStorePush", "Device Push Token: $token")

        // Сохраняем токен через useCase
        CoroutineScope(Dispatchers.IO).launch {
            try {
                savePushTokenUseCase(token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
//        // Отображаем уведомление на устройстве
//        val notificationManager =
//            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        val channelId = "default_push_channel"
//
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setContentTitle(message.notification?.title ?: "Push Title")
//            .setContentText(message.notification?.body ?: "Push Body")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setAutoCancel(true)
//            .build()
//
//        notificationManager.notify(message.hashCode(), notification)
//        Log.d("RuStorePush", "Push notification displayed: ${message.notification?.title}")
//    }

}


