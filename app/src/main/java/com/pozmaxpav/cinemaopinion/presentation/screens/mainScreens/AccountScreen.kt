package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.MyDropdownMenuItem
import com.pozmaxpav.cinemaopinion.presentation.components.SettingsMenu
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.AccountListItem
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

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

    LaunchedEffect(userId) {
        auxiliaryUserViewModel.getUserData(userId)
        auxiliaryUserViewModel.getAwardsList(userId)
        Log.d("@@@", listAwards)
    }

    Card(
        modifier = Modifier
            .fillMaxHeight(0.7f),
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

            AccountSettingMenu(navController)

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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                )

                AccountListItem(
                    icon = painterResource(id = R.drawable.ic_movie_list),
                    contentDescription = stringResource(id = R.string.description_icon_movie_list),
                    title = stringResource(id = R.string.my_list_movies)
                ) {
                    navigateFunction(navController, Route.ListSelectedMovies.route)
                }

                Spacer(modifier = Modifier.height(20.dp))

                AccountListItem(
                    icon = painterResource(id = R.drawable.ic_movie_list),
                    contentDescription = stringResource(id = R.string.description_icon_movie_list),
                    title = stringResource(id = R.string.joint_list_films)
                ) {
                    navigateFunction(navController, Route.ListSelectedGeneralMovies.route)
                }

                Spacer(modifier = Modifier.height(20.dp))

                AccountListItem(
                    icon = painterResource(id = R.drawable.ic_movie_list),
                    contentDescription = stringResource(id = R.string.description_icon_serials_list),
                    title = stringResource(id = R.string.joint_list_serials)
                ) {
                    navigateFunction(navController, Route.ListSelectedGeneralSerials.route)
                }

                Spacer(modifier = Modifier.height(20.dp))

                AccountListItem(
                    icon = painterResource(id = R.drawable.ic_movie_list),
                    contentDescription = "null",
                    title = "Контроль серий"
                ) {
                    navigateFunction(navController, Route.SeriesControlScreen.route)
                }

                // region Awards
//                Column(
//                    modifier = Modifier.padding(10.dp).weight(1f),
//                    verticalArrangement = Arrangement.Bottom
//                ) {
//                    if (listAwards.isNotEmpty()) {
//                        Text(
//                            text = "Зал славы",
//                            style = MaterialTheme.typography.displayMedium,
//                            modifier = Modifier
//                                .padding(bottom = 16.dp)
//                                .align(alignment = Alignment.CenterHorizontally)
//                        )
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.Center,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            val newListAwards = listAwards.split(",")
//                            for (i in newListAwards) {
//                                Image(
//                                    painter = painterResource(id = i.toInt()),
//                                    contentDescription = null,
//                                    modifier = Modifier.height(70.dp)
//                                )
//                            }
//                        }
//                    }
//                }
                // endregion
            }
        }
    }

}


@Composable
private fun AccountSettingMenu(navController: NavHostController) {
    SettingsMenu { closeMenu ->

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
    }
}


