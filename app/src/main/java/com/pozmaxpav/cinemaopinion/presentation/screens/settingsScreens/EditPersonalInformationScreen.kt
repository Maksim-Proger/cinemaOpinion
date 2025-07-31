package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.fab.FabButton
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextField
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonalInformationScreen(
    navController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val userId by mainViewModel.userId.collectAsState()
    val userData by userViewModel.userData.collectAsState()

    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }

    val (nikName, setNikName) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    // Запускаем LaunchedEffect только для обновления значений, если userData не null.
    LaunchedEffect(userData) {
        userData?.let { user ->
            setNikName(user.nikName)
            setEmail(user.email)
            setPassword(user.password)
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ClassicTopAppBar(
                context = context,
                titleId = R.string.top_app_bar_header_name_user_information,
                scrollBehavior = scrollBehavior,
                onTransitionAction = { navigateFunction(navController, Route.MainScreen.route) }
            )
        },
        floatingActionButton = {
            FabButton(
                imageIcon = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.description_floating_action_button_save),
                textFloatingButton = stringResource(id = R.string.floating_action_button_save),
                onButtonClick = {
                    userViewModel.updatingUserData(userData?.id!!, nikName, email, password)
                    navigateFunction(navController, Route.MainScreen.route)
                    showToast(context, R.string.edit_personal_information)
                },
                expanded = true
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
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