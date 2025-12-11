package com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ui.presentation.components.topappbar.TopAppBarAllScreens
import com.example.ui.presentation.components.fab.FABMenu
import com.example.ui.presentation.components.text.CustomTextField
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    systemViewModel: SystemViewModel,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val userId by systemViewModel.userId.collectAsState()
    val userData by userViewModel.userData.collectAsState()

    val (nikName, setNikName) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        systemViewModel.getUserId()
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }
    LaunchedEffect(userData) {
        userData?.let { user ->
            setNikName(user.nikName)
            setEmail(user.email)
            setPassword(user.password)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarAllScreens(
                context = context,
                titleId = R.string.top_app_bar_header_name_user_information,
                scrollBehavior = scrollBehavior,
                onTransitionAction = { navigateFunction(navController, Route.MainScreen.route) }
            )
        },
        floatingActionButton = {
            FABMenu(
                imageIcon = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.description_floating_action_button_save),
                onButtonClick = {
                    userViewModel.updatingUserData(userId, nikName, email, password)
                    navigateFunction(navController, Route.MainScreen.route)
                    showToast(context, R.string.edit_personal_information)
                },
                expanded = false
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // region nikName
            CustomTextField(
                value = nikName,
                onValueChange = setNikName,
                label = { Text(stringResource(id = R.string.text_for_edit_nik_name_field)) },
                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_nik_name_field)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_name),
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_nik_name_field)) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
            // endregion

            // region Email
            CustomTextField(
                value = email,
                onValueChange = setEmail,
                label = { Text(stringResource(id = R.string.text_for_edit_email_field)) },
                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_email_field)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_lastname),
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_email_field)) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
            // endregion

            // region Password
            CustomTextField(
                value = password,
                onValueChange = setPassword,
                label = { Text(stringResource(id = R.string.text_for_edit_password_field)) },
                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_password_field)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_lastname),
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_password_field)) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
            // endregion
        }
    }
}
