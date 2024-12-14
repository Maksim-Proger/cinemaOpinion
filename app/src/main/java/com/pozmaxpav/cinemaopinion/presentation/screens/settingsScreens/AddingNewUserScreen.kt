package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.FabButton
import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.UserViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextField
import com.pozmaxpav.cinemaopinion.utilits.showToast
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingNewUserScreen(
    nameToast: String,
    onClickClose: () -> Unit,
    viewModel: UserViewModel = hiltViewModel(),
    viewModelFirebase: FirebaseViewModel = hiltViewModel()
) {
    val (firstName, setFirstName) = remember { mutableStateOf("") }
    val (lastName, setLastName) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxHeight(0.7f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ClassicTopAppBar(
                    title = stringResource(R.string.title_for_adding_new_user_screen),
                    scrollBehavior = scrollBehavior,
                    onTransitionAction = onClickClose
                )
            },
            floatingActionButton = {
                FabButton(
                    imageIcon = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.description_floating_action_button_save),
                    textFloatingButton = stringResource(id = R.string.floating_action_button_save),
                    onButtonClick = {
                        val newUser = DomainUser(
                            UUID.randomUUID().toString(),
                            firstName,
                            lastName
                        )
                        viewModel.addUser(newUser)
                        viewModelFirebase.updatingUserData(newUser)
                        onClickClose()
                        showToast(context, nameToast)
                    },
                    expanded = true
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
                CustomTextField(
                    value = firstName,
                    onValueChange = setFirstName,
                    label = { Text(stringResource(id = R.string.text_for_add_name_field)) },
                    placeholder = { Text(stringResource(id = R.string.placeholder_for_add_name_field)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(id = R.string.description_icon_add_name),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    },
                    supportingText = { Text(stringResource(id = R.string.placeholder_for_add_name_field)) },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )

                CustomTextField(
                    value = lastName,
                    onValueChange = setLastName,
                    label = { Text(stringResource(id = R.string.text_for_add_lastName_field)) },
                    placeholder = { Text(stringResource(id = R.string.placeholder_for_add_lastName_field)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(id = R.string.description_icon_add_lastName_name),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    },
                    supportingText = { Text(stringResource(id = R.string.placeholder_for_add_lastName_field)) },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
            }
        }
    }
}