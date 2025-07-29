package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.CustomBoxShowOverlay
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTextButton
import com.pozmaxpav.cinemaopinion.presentation.components.MyDropdownMenuItem
import com.pozmaxpav.cinemaopinion.presentation.components.SettingsMenu
import com.pozmaxpav.cinemaopinion.presentation.components.ShowSharedLists
import com.pozmaxpav.cinemaopinion.presentation.components.items.AccountItem
import com.pozmaxpav.cinemaopinion.presentation.components.items.SharedListItem
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextField
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.navigateFunctionClearAllScreens
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    navController: NavHostController,
    onClick: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel(),
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel()
) {

    val userId by mainViewModel.userId.collectAsState()
    val userData by auxiliaryUserViewModel.userData.collectAsState()
    val listAwards by auxiliaryUserViewModel.listAwards.collectAsState()
    var locationShowDialogEvents by remember { mutableStateOf(false) }
    var openSharedLists by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (userId != "Unknown") {
            auxiliaryUserViewModel.getUserData(userId)
            auxiliaryUserViewModel.getAwardsList(userId)
        }
    }

    Card(
        modifier = Modifier.fillMaxHeight(0.9f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = stringResource(id = R.string.title_account_screen),
                style = MaterialTheme.typography.displayLarge
            )

            AccountSettingMenu(
                navController,
                mainViewModel,
                openDialog = { locationShowDialogEvents = true }
            )

        }

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(60.dp),
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        colorFilter = ColorFilter
                            .tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    if (userData != null) {
                        Column {
                            userData?.let { user ->
                                Text(
                                    text = user.nikName,
                                    style = MaterialTheme.typography.displayMedium
                                )
                                Text(
                                    text = user.email,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                AccountItem(
                    icon = painterResource(id = R.drawable.ic_movie_list),
                    contentDescription = stringResource(id = R.string.description_icon_movie_list),
                    title = stringResource(id = R.string.my_list_movies)
                ) { navigateFunction(navController, Route.ListSelectedMovies.route) }

                Spacer(modifier = Modifier.height(20.dp))

                AccountItem(
                    icon = painterResource(id = R.drawable.ic_movie_list),
                    contentDescription = stringResource(id = R.string.description_icon_movie_list),
                    title = stringResource(id = R.string.joint_list_films)
                ) { navigateFunction(navController, Route.ListSelectedGeneralMovies.route) }

                Spacer(modifier = Modifier.height(20.dp))

                AccountItem(
                    icon = painterResource(id = R.drawable.ic_movie_list),
                    contentDescription = stringResource(id = R.string.description_icon_serials_list),
                    title = stringResource(id = R.string.joint_list_serials)
                ) { navigateFunction(navController, Route.ListSelectedGeneralSerials.route) }

                Spacer(modifier = Modifier.height(20.dp))

                AccountItem(
                    icon = painterResource(id = R.drawable.ic_movie_list),
                    contentDescription = stringResource(R.string.description_icon_series_control),
                    title = stringResource(R.string.series_control)
                ) { navigateFunction(navController, Route.SeriesControlScreen.route) }

                Spacer(modifier = Modifier.height(20.dp))

                AccountItem(
                    icon = painterResource(id = R.drawable.ic_movie_list),
                    contentDescription = stringResource(R.string.description_icon_shared_lists),
                    title = stringResource(R.string.shared_lists)
                ) { openSharedLists = true }

                // region Awards
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (listAwards.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Зал славы",
                                style = MaterialTheme.typography.displayMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val newListAwards = listAwards.split(",")
                            for (i in newListAwards) {
                                Image(
                                    painter = painterResource(id = i.toInt()),
                                    contentDescription = null,
                                    modifier = Modifier.height(70.dp)
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column {
                                Text(
                                    text = "Зал славы",
                                    style = MaterialTheme.typography.displayMedium,
                                    modifier = Modifier
                                        .padding(bottom = 16.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Наград пока нет",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(bottom = 16.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                            }
                        }

                    }
                }
                // endregion
            }
        }
    }

    if (openSharedLists) {
        CustomBoxShowOverlay(
            onDismiss = { openSharedLists = false },
            paddingVerticalSecondBox = 150.dp,
            paddingHorizontalSecondBox = 36.dp,
            content = {
                ShowSharedLists(
                    navController = navController,
                    userId = userId
                )
            }
        )
    }

    if (locationShowDialogEvents) {
        SharedListAlertDialog(
            userId = userId,
            onDismiss = { locationShowDialogEvents = false }
        )
    }

}

@Composable
private fun AccountSettingMenu(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel(),
    openDialog: () -> Unit = {}
) {

    val coroutineScope = rememberCoroutineScope()

    SettingsMenu { closeMenu ->

        MyDropdownMenuItem(
            onAction = openDialog,
            title = stringResource(R.string.drop_down_menu_create_shared_list),
            leadingIcon = {
                Icon(
                    Icons.Default.Create,
                    contentDescription = stringResource(R.string.description_icon_create_shared_list),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        )
        HorizontalDivider()
        MyDropdownMenuItem(
            onAction = {
                navigateFunction(navController, Route.EditPersonalInformationScreen.route)
                closeMenu() // Закрываем меню после навигации
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
        HorizontalDivider()
        MyDropdownMenuItem(
            onAction = {
                navigateFunction(navController, Route.SettingsScreen.route)
                closeMenu()
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
        HorizontalDivider()
        MyDropdownMenuItem(
            onAction = {
                coroutineScope.launch {
                    mainViewModel.clearUserData()
                    auxiliaryUserViewModel.clearFlag()
                    navigateFunctionClearAllScreens(navController, Route.LoginScreen.route)
                    closeMenu()
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

@Composable
private fun SharedListAlertDialog(
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
                    sharedListsViewModel.createList(
                        title = title,
                        userCreatorId = userId,
                        invitedUserAddress = invitedUserAddress
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
                    imeAction = ImeAction.Done
                )
            }
        }
    )
}
