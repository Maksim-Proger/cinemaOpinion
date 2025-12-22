package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ui.presentation.components.topappbar.TopAppBarAllScreens
import com.example.ui.presentation.theme.CommentAddedColor
import com.example.ui.presentation.theme.DeveloperCommentColor
import com.example.ui.presentation.theme.FilmAddedColor
import com.example.ui.presentation.theme.FilmDeleteColor
import com.example.ui.presentation.theme.MovingElement
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.NotificationViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfChangesScreen(
    navController: NavHostController,
    systemViewModel: SystemViewModel,
    userViewModel: UserViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    val list by notificationViewModel.notifications.collectAsState()
    val userId by systemViewModel.userId.collectAsState()
    val userData by userViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        notificationViewModel.getNotifications(userId)
        systemViewModel.getUserId()
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }

    Scaffold(
        topBar = {
            TopAppBarAllScreens(
                context = context,
                titleId = R.string.title_list_of_changes_series,
                scrollBehavior = scrollBehavior,
                onTransitionAction = {
                    navigateFunction(navController, Route.MainScreen.route)
                }
            )
        },
    ) { innerPadding ->
        if (list.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(10.dp)
            ) {
                itemsIndexed(list.reversed()) { _, item ->
                    val color = colorMethod(item.noteText)
                    val icon = iconMethod(item.noteText)

                    NotificationItem(
                        modifier = Modifier.animateItem(),
                        icon = icon,
                        color = color,
                        it = item
                    ) {
                        userData?.let { user ->
                            navController.navigate(
                                Route.MovieDetailScreen.createRoute(
                                    listId = item.sharedListId,
                                    movieId = item.entityId,
                                    userName = user.nikName
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.developer_message2),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun NotificationItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color,
    it: DomainNotificationModel,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                tint = color,
                contentDescription = null,
                modifier = Modifier.padding(7.dp),
            )

            Column(
                modifier = Modifier.padding(7.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (it.username.contains("Разработчик")) {
                    Text(
                        text = stringResource(R.string.developer_message),
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Изменения от: ",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                .format(Date(it.timestamp)),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 7.dp))
                Text(
                    text = "${it.username} ${it.noteText}",
                    style = MaterialTheme.typography.bodyLarge
                )

            }
        }
    }
}

fun colorMethod(text: String): Color {
    return when {
        text.contains("добавил важный комментарий") -> DeveloperCommentColor
        text.contains("добавил(а) комментарий") -> CommentAddedColor
        text.contains("добавил(а) список") -> CommentAddedColor
        text.contains("удалил(а) фильм") -> FilmDeleteColor
        text.contains("добавил(а)") -> FilmAddedColor
        else -> Color.Transparent
    }
}

fun iconMethod(text: String): ImageVector {
    return when {
        text.contains("добавил важный комментарий") -> Icons.Default.PriorityHigh
        text.contains("добавил(а) комментарий") -> Icons.Default.CommentBank
        text.contains("удалил(а) фильм") -> Icons.Default.Delete
        text.contains("добавил(а)") -> Icons.Default.Add
        else -> Icons.Default.CommentBank
    }
}

