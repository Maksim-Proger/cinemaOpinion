package com.pozmaxpav.cinemaopinion.presentation.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.SettingsMenu
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
                .fillMaxHeight(0.5f),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 25.dp, vertical = 15.dp)
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
                    SettingsMenu(navController)
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
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
                                Text(text = "Максим Позданяков")
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

//@Preview(showBackground = true)
//@Composable
//fun AccountScreenPreview() {
//    CinemaOpinionTheme {
//        AccountScreen(onClick = { /* Действие при клике */ })
//    }
//}
