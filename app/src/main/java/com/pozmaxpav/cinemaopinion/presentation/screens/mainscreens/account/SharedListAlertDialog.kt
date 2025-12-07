package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.text.CustomTextField
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.MovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel

@Composable
fun SharedListAlertDialog(
    userId: String,
    sharedListsViewModel: SharedListsViewModel = hiltViewModel(),
    movieViewModel: MovieViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val userData by userViewModel.userData.collectAsState()

    val (title, setTitle) = remember { mutableStateOf("") }
    val (invitedUserAddress, setInvitedUserAddress) = remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            CustomTextButton(
                textButton = stringResource(R.string.button_create_shared_list),
                textStyle = MaterialTheme.typography.bodyLarge,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                onClickButton = {
                    val invitedUserAddressSplit = invitedUserAddress
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }

                    userData?.let { user ->
                        sharedListsViewModel.createNotification(
                            context = context,
                            username = user.nikName,
                            stringResourceId = R.string.record_add_the_list,
                            title = title,
                            newDataSource = context.getString(R.string.list_was_deleted)
                        )
                    }

                    sharedListsViewModel.createList(
                        title = title,
                        userCreatorId = userId,
                        invitedUserAddress = invitedUserAddressSplit
                    )
                    onDismiss()
                }
            )
        },
        text = {
            Column {
                CustomTextField(
                    value = title,
                    onValueChange = setTitle,
                    label = {
                        Text(
                            text = stringResource(R.string.text_for_field_create_shared_list),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
                CustomTextField(
                    value = invitedUserAddress,
                    verticalPadding = 0.dp,
                    onValueChange = setInvitedUserAddress,
                    label = {
                        Text(
                            text = stringResource(R.string.text_for_field_create_shared_list_invited_user_address),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    singleLine = false
                )
            }
        }
    )
}
