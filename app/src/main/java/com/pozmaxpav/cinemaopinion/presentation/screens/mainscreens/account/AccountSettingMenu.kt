package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.account

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.MyDropdownMenuItem
import com.pozmaxpav.cinemaopinion.presentation.components.SettingsMenu
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.navigateFunctionClearAllScreens
import kotlinx.coroutines.launch

@Composable
fun AccountSettingMenu(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    userViewModel: UserViewModel = hiltViewModel(),
    openDialog: () -> Unit = {}
) {
    var triggerOnClickCloseMenu by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    SettingsMenu { closeMenu ->

        LaunchedEffect(triggerOnClickCloseMenu) { if (triggerOnClickCloseMenu) { closeMenu() } }

        MyDropdownMenuItem(
            onAction = {
                openDialog()
                triggerOnClickCloseMenu = true
            },
            title = stringResource(R.string.drop_down_menu_create_shared_list),
            leadingIcon = {
                Icon(
                    Icons.Default.Create,
                    contentDescription = stringResource(R.string.description_icon_create_shared_list),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
        MyDropdownMenuItem(
            onAction = {
                navigateFunction(navController, Route.EditPersonalInformationScreen.route)
                triggerOnClickCloseMenu = true
            },
            title = stringResource(id = R.string.drop_down_menu_item_edit),
            leadingIcon = {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.description_icon_edit),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
        MyDropdownMenuItem(
            onAction = {
                navigateFunction(navController, Route.SettingsScreen.route)
                triggerOnClickCloseMenu = true
            },
            title = stringResource(id = R.string.drop_down_menu_item_settings),
            leadingIcon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.description_icon_settings),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
        MyDropdownMenuItem(
            onAction = {
                coroutineScope.launch {
                    mainViewModel.logout { userViewModel.clearFlag() }
                    navigateFunctionClearAllScreens(navController, Route.LoginScreen.route)
                    triggerOnClickCloseMenu = true
                }
            },
            title = stringResource(R.string.drop_down_menu_item_exit),
            leadingIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(id = R.string.description_icon_exit),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        )
    }
}