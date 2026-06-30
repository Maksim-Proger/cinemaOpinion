package com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ui.presentation.components.topappbar.TopAppBarAllScreens
import com.example.ui.presentation.components.fab.FABMenu
import com.example.ui.presentation.components.text.CustomTextField
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.AvatarImage
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.AvatarUploadError
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilities.navigateFunction
import com.pozmaxpav.cinemaopinion.utilities.showToast

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
    val avatarPreviewUri by userViewModel.avatarPreviewUri.collectAsState()
    val isUploadingAvatar by userViewModel.isUploadingAvatar.collectAsState()

    val (nikName, setNikName) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    val pickAvatarLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null && userId != "Unknown") {
            userViewModel.uploadAvatar(userId, uri)
        }
    }

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
    LaunchedEffect(Unit) {
        userViewModel.avatarUploadError.collect { error ->
            val messageId = when (error) {
                AvatarUploadError.TOO_LARGE -> R.string.avatar_error_too_large
                AvatarUploadError.INVALID_FILE -> R.string.avatar_error_invalid_file
                AvatarUploadError.NETWORK -> R.string.avatar_error_network
                AvatarUploadError.UNKNOWN -> R.string.avatar_error_unknown
            }
            showToast(context, messageId)
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
                imageIcon = Icons.Default.Done,
                contentDescription = stringResource(id = R.string.description_floating_action_button_save),
                onButtonClick = {
                    if (userData != null && userId != "Unknown") {
                        userViewModel.updatingUserData(userId, nikName, email, password)
                        navigateFunction(navController, Route.MainScreen.route)
                        showToast(context, R.string.edit_personal_information)
                    }
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
            verticalArrangement = Arrangement.Top
        ) {

            // region Аватарка
            Box(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .size(120.dp)
            ) {
                AvatarImage(
                    userId = userId,
                    previewUri = avatarPreviewUri,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable(enabled = !isUploadingAvatar) {
                            pickAvatarLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                )

                if (isUploadingAvatar) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.description_change_avatar),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(6.dp)
                    )
                }
            }
            // endregion

            // region nikName
            CustomTextField(
                value = nikName,
                onValueChange = setNikName,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.text_for_edit_nik_name_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.placeholder_for_edit_nik_name_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_name),
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                supportingText = {
                    Text(
                        text = stringResource(id = R.string.placeholder_for_edit_nik_name_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
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
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.text_for_edit_email_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.placeholder_for_edit_email_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_lastname),
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                supportingText = {
                    Text(
                        text = stringResource(id = R.string.placeholder_for_edit_email_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
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
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.text_for_edit_password_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.placeholder_for_edit_password_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_lastname),
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                supportingText = {
                    Text(
                        text = stringResource(id = R.string.placeholder_for_edit_password_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
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
