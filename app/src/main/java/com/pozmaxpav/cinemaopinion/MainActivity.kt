package com.pozmaxpav.cinemaopinion

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.auth.presentation.navigation.AuthRoute
import com.example.intro.presentation.introscreens.viewmodel.OnBoardingViewModel
import com.example.intro.presentation.navigation.IntroRoute
import com.example.ui.presentation.theme.AppTheme
import com.example.ui.presentation.viewmodels.ThemeViewModel
import com.pozmaxpav.cinemaopinion.presentation.navigation.NavGraph
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilities.CheckAndUpdateAppVersion
import com.pozmaxpav.cinemaopinion.utilities.LoadingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Launcher для запроса разрешения
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Разрешение предоставлено
                android.util.Log.d("RuStorePush", "POST_NOTIFICATIONS разрешение предоставлено")
            } else {
                // Разрешение отклонено
                android.util.Log.d("RuStorePush", "POST_NOTIFICATIONS разрешение отклонено")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Запрос разрешения на пуши (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            val context = LocalContext.current

            // TODO: Переработать, чтобы уменьшить уровень привязки. Убрать мерцание.
            val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
            val hasEntered by onBoardingViewModel.hasUserEnteredApp.collectAsState(initial = null)

            val themeViewModel: ThemeViewModel = hiltViewModel()
            val systemViewModel: SystemViewModel = hiltViewModel()

            val registrationFlag by systemViewModel.registrationFlag.collectAsState()

            // Получаем "destination" из ярлыка
            val destinationFromShortcut = intent.extras?.getString("destination")
            val destination = destinationFromShortcut
                ?: if (registrationFlag) Route.MainScreen.route else AuthRoute.LOGIN_SCREEN

            AppTheme(themeViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when(hasEntered) {
                        true -> {
                            CheckAndUpdateAppVersion(context)
                            NavGraph(
                                themeViewModel = themeViewModel,
                                systemViewModel = systemViewModel,
                                startDestination = destination
                            )
                        }
                        false -> {
                            NavGraph(
                                themeViewModel = themeViewModel,
                                systemViewModel = systemViewModel,
                                startDestination = IntroRoute.ON_BOARDING_SCREEN
                            )
                        }
                        null -> LoadingScreen()
                    }
                }
            }
        }
    }
}

