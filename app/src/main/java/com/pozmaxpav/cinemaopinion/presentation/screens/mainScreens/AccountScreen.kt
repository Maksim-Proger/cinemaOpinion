package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.MyDropdownMenuItem
import com.pozmaxpav.cinemaopinion.presentation.components.SettingsMenu
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.theme.CinemaOpinionTheme
import com.pozmaxpav.cinemaopinion.utilits.AccountListItem

@Composable
fun AccountScreen(
    navController: NavHostController,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp)
                .fillMaxHeight(0.5f),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.color_account_card),
                contentColor = colorResource(id = R.color.color_text_account_card)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 25.dp,
                        vertical = 15.dp
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.title_account_screen),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    AccountSettingMenu(navController)

                }
                Spacer(modifier = Modifier.padding(8.dp))
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.color_content_account_card),
                        contentColor = colorResource(id = R.color.color_text_account_card)
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
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
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Column {
                                Text(text = "Максим Поздняков")
                                Text(
                                    text = "z@yandex.ru",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(20.dp))
                        AccountListItem(
                            icon = painterResource(id = R.drawable.ic_movie_list),
                            contentDescription = stringResource(id = R.string.description_icon_movie_list),
                            title = stringResource(id = R.string.my_list_movies)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        AccountListItem(
                            icon = painterResource(id = R.drawable.ic_movie_list),
                            contentDescription = stringResource(id = R.string.description_icon_movie_list),
                            title = stringResource(id = R.string.joint_list_films)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun AccountSettingMenu(navController: NavHostController) {
    SettingsMenu { closeMenu ->

        MyDropdownMenuItem(
            onNavigate = { // TODO: Надо детальнее разобраться в этой навигации
                navController.navigate(Route.EditPersonalInformationScreen.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                closeMenu() // Закрываем меню после навигации
            },
            title = stringResource(id = R.string.drop_down_menu_item_edit),
            leadingIcon = {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.description_icon_edit)
                )
            }
        )

        MyDropdownMenuItem(
            onNavigate = { /* Настроить навигацию */ },
            title = stringResource(id = R.string.drop_down_menu_item_settings),
            leadingIcon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.description_icon_settings)
                )
            }
        )

        MyDropdownMenuItem(
            onNavigate = { /* Настроить навигацию */ },
            title = stringResource(id = R.string.drop_down_menu_item_exit),
            leadingIcon = {
                Icon(
                    Icons.Default.ExitToApp,
                    contentDescription = stringResource(id = R.string.description_icon_exit)
                )
            }
        )
    }
}


