package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.items.AccountItem
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.SharedListsScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

@Composable
fun AccountScreen(
    navController: NavHostController,
    userId: String,
    systemViewModel: SystemViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    val userData by userViewModel.userData.collectAsState()
    val listAwards by userViewModel.listAwards.collectAsState()
    var locationShowDialogEvents by remember { mutableStateOf(false) }
    var openSharedLists by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    LaunchedEffect(userId) {
        if (userId != "Unknown") {
            userViewModel.getUserData(userId)
            userViewModel.getAwardsList(userId)
        }
    }

    Card(
        modifier = Modifier.fillMaxHeight(1f),
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
            userData?.let { user ->
                AccountSettingMenu(
                    userName = user.nikName,
                    navController = navController,
                    systemViewModel = systemViewModel,
                    openDialog = { locationShowDialogEvents = true }
                )
            }
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

                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .verticalScroll(scrollState)
                ) {
                    AccountItem(
                        icon = painterResource(id = R.drawable.ic_movie_list),
                        contentDescription = stringResource(id = R.string.description_icon_movie_list),
                        title = stringResource(id = R.string.my_list_movies)
                    ) { navigateFunction(navController, Route.ListSelectedMovies.route) }
                    Spacer(modifier = Modifier.height(20.dp))
                    AccountItem(
                        icon = painterResource(id = R.drawable.ic_movie_list),
                        contentDescription = stringResource(R.string.description_icon_shared_lists),
                        title = stringResource(R.string.shared_lists)
                    ) { openSharedLists = true }
                    Spacer(modifier = Modifier.height(20.dp))
                    AccountItem(
                        icon = painterResource(id = R.drawable.ic_movie_list),
                        contentDescription = stringResource(R.string.description_icon_series_control),
                        title = stringResource(R.string.series_control)
                    ) { navigateFunction(navController, Route.SeriesControlScreen.route) }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))
                // region AwardsFields
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    TextAwardsFields(listAwards)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (listAwards.isNotEmpty()) {
                            val newListAwards = listAwards.split(",")
                            for (i in newListAwards) {
                                Image(
                                    painter = painterResource(id = i.toInt()),
                                    contentDescription = null,
                                    modifier = Modifier.height(70.dp)
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
        Box(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { openSharedLists = false }

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                userData?.let { user ->
                    SharedListsScreen(
                        navController = navController,
                        userId = userId,
                        userName = user.nikName
                    )
                }
            }
        }
    }

    if (locationShowDialogEvents) {
        SharedListAlertDialog(
            userId = userId,
            onDismiss = { locationShowDialogEvents = false }
        )
    }

}

