package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.account

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilities.navigateFunction
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.SharedListsScreen


// region Цвета
private val BgDark = Color(0xFF1C1209)
private val CardDark = Color(0xFF2A1E0F)
private val GlassWhite = Color(0x33FFFFFF)
private val GlassBorder = Color(0x55FFFFFF)
private val TextWhite = Color(0xFFFFFFFF)
private val TextSubtle = Color(0xFFB8A08A)
private val TextMuted = Color(0xFF7A6A55)
// endregion

@Composable
fun NewAccountScreen(
    navController: NavHostController,
    userId: String,
    onClose: () -> Unit,
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    systemViewModel: SystemViewModel = hiltViewModel()
) {
    val listSelectedMovies by personalMovieViewModel.listSelectedMovies.collectAsState()
    val userData by userViewModel.userData.collectAsState()
    val listAwards by userViewModel.listAwards.collectAsState()
    var openSharedLists by remember { mutableStateOf(false) }
    var locationShowDialogEvents by remember { mutableStateOf(false) }
    var settingsMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (userId != "Unknown") {
            userViewModel.getUserData(userId)
            userViewModel.getAwardsList(userId)
        }
    }
    LaunchedEffect(userId) {
        personalMovieViewModel.getMovies(userId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            userData?.let { user ->
                HeroSection(
                    name = user.nikName,
                    email = user.email,
                    listAwards = listAwards,
                    settingsButton = {
                        Box {
                            GlassButton(
                                icon = Icons.Default.MoreVert,
                                onClick = { settingsMenuExpanded = true }
                            )
                            AccountSettingMenu(
                                userName = user.nikName,
                                customIcon = true,
                                expanded = settingsMenuExpanded,
                                onExpandedChange = { settingsMenuExpanded = it },
                                navController = navController,
                                systemViewModel = systemViewModel,
                                openDialog = { locationShowDialogEvents = true }
                            )
                        }
                    },
                    closeScreenButton = onClose
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ListsRow(
                    label = "Личный список",
                    imageRes = R.drawable.personal_list,
                    listSelectedMovies = listSelectedMovies,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navigateFunction(navController, Route.ListSelectedMovies.route)
                    }
                )
                ListsRow(
                    label = "Совместные список",
                    imageRes = R.drawable.shared_list,
                    listSelectedMovies = listSelectedMovies,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        openSharedLists = true
                    }
                )
                ListsRow(
                    label = "Контроль серий",
                    imageRes = R.drawable.series_control,
                    listSelectedMovies = listSelectedMovies,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navigateFunction(navController, Route.SeriesControlScreen.route)
                    }
                )
            }
        }
    }

    if (openSharedLists) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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

@Composable
fun ListsRow(
    label: String,
    @DrawableRes imageRes: Int,
    listSelectedMovies: List<DomainSelectedMovieModel>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = listSelectedMovies.size.toString(),
                color = TextWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = label,
                color = TextSubtle,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun HeroSection(
    photoUrl: String = "",
    name: String,
    email: String,
    listAwards: String = "",
    settingsButton: @Composable () -> Unit,
    closeScreenButton: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(520.dp) // TODO: Убрать фиксированную высоту.
    ) {
        // region Фото
//        AsyncImage(
//            model = photoUrl,
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
        // endregion

        // region Верхние кнопки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlassButton(
                icon = Icons.Default.Close,
                onClick = closeScreenButton
            )
            settingsButton()
        }
        // endregion

        // region Имя
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                color = TextWhite,
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = email,
                color = TextSubtle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(16.dp))

            Achievements(listAwards = listAwards)
        }
        // endregion


    }
}

@Composable
private fun GlassButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.height(42.dp),
        shape = RoundedCornerShape(50),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = SolidColor(GlassBorder)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = GlassWhite,
            contentColor = TextWhite
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun Achievements(
    listAwards: String,
    onClick: () -> Unit = {}
) {
    val newListAwards = listAwards.split(",")

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = SolidColor(GlassBorder)
        ),
    ) {
        Text(
            text = "${newListAwards.size} achievements",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }


}





//Text(
//text = stringResource(id = R.string.title_account_screen),
//style = MaterialTheme.typography.displayLarge
//)

//// region AwardsFields
//Column(
//modifier = Modifier.padding(10.dp),
//verticalArrangement = Arrangement.Bottom
//) {
//    TextAwardsFields(listAwards)
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        if (listAwards.isNotEmpty()) {
//            val newListAwards = listAwards.split(",")
//            for (i in newListAwards) {
//                Image(
//                    painter = painterResource(id = i.toInt()),
//                    contentDescription = null,
//                    modifier = Modifier.height(70.dp)
//                )
//            }
//        }
//    }
//}
//// endregion















