package com.pozmaxpav.cinemaopinion.utilits

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel

@Composable
fun CheckAndUpdateAppVersion(
    context: Context,
    viewModel: SystemViewModel = hiltViewModel()
) {
    val packageInfo = try {
        context.packageManager.getPackageInfo(context.packageName, 0)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    val currentVersionApp = packageInfo?.versionName
    val savedVersionApp by viewModel.versionApp.collectAsState()

    LaunchedEffect(currentVersionApp, savedVersionApp) {
        if (currentVersionApp == null) return@LaunchedEffect

        if (currentVersionApp != savedVersionApp) {
            viewModel.saveAppVersion(currentVersionApp)
            viewModel.saveResultChecking(false)
        } else {
            viewModel.saveResultChecking(true)
        }
    }

}