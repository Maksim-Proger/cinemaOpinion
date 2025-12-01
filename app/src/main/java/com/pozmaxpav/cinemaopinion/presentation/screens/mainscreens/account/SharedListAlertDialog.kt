package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.text.CustomTextField
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel

@Composable
fun SharedListAlertDialog(
    userId: String,
    sharedListsViewModel: SharedListsViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val (title, setTitle) = remember { mutableStateOf("") }
    val (invitedUserAddress, setInvitedUserAddress) = remember { mutableStateOf("") }

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
