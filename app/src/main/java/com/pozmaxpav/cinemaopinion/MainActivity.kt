package com.pozmaxpav.cinemaopinion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.auth.presentation.navigation.AuthRoute
import com.example.core.utils.haptic.HapticManagerProvider
import com.example.core.utils.sound.SoundManagerProvider
import com.example.intro.presentation.introscreens.viewmodel.OnBoardingViewModel
import com.example.intro.presentation.navigation.IntroRoute
import com.example.ui.presentation.screens.splash.SplashScreen
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
                android.util.Log.d("RuStorePush", "POST_NOTIFICATIONS разрешение предоставлено")
            } else {
                android.util.Log.d("RuStorePush", "POST_NOTIFICATIONS разрешение отклонено")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setOnExitAnimationListener { provider -> provider.remove() }
        enableEdgeToEdge()

        // Запрос разрешения на получение push (Android 13+)
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

            var splashVisible by rememberSaveable { mutableStateOf(true) }

            // Получаем "destination" из ярлыка
            val destinationFromShortcut = intent.extras?.getString("destination")
            val destination = destinationFromShortcut
                ?: if (registrationFlag) Route.MainScreen.route else AuthRoute.LOGIN_SCREEN

            AppTheme(themeViewModel) {
                SoundManagerProvider {
                    HapticManagerProvider {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                when (hasEntered) {
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

                                AnimatedVisibility(
                                    visible = splashVisible,
                                    enter = EnterTransition.None,
                                    exit = fadeOut(animationSpec = tween(450))
                                ) {
                                    SplashScreen(
                                        isContentReady = hasEntered != null,
                                        onFinished = { splashVisible = false }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}

