package com.pozmaxpav.cinemaopinion.utilits

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel

@Composable
fun CheckAndUpdateAppVersion(
    context: Context,
    viewModel: MainViewModel = hiltViewModel()
) {

    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val currentVersionApp = packageInfo.versionName
    val savedVersionApp by viewModel.versionApp.collectAsState()

    if (currentVersionApp != savedVersionApp) {
        viewModel.saveAppVersion(currentVersionApp.toString()) // TODO: Почему toString()
        viewModel.saveResultChecking(false)
    } else {
        viewModel.saveResultChecking(true)
    }

}