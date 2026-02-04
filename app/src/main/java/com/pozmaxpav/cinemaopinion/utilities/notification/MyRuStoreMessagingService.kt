package com.pozmaxpav.cinemaopinion.utilities.notification

import android.provider.Settings
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetUserIdUseCase
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
}
