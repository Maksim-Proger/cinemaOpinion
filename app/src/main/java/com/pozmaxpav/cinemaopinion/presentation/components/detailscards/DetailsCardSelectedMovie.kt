package com.pozmaxpav.cinemaopinion.presentation.components.detailscards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ui.presentation.components.ExpandedCard
import com.example.ui.presentation.theme.DynamicContentColor
import com.example.ui.presentation.theme.RatingBadgeColor
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.SharedListsScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SeriesControlViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.utilities.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsCardSelectedMovie(
    movie: DomainSelectedMovieModel,
    userId: String = "",
    navController: NavHostController,

    sendToWaitingList: () -> Unit = {},
    sendToArchive: @Composable () -> Unit = {},

    needSkipButton: Boolean = true,
    needFavoritesButton: Boolean = true,

    needReviews: Boolean = true,
    reviews: () -> Unit = {},


    apiViewModel: ApiViewModel = hiltViewModel(),
    personalViewModel: PersonalMovieViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    seriesViewModel: SeriesControlViewModel = hiltViewModel(),
    onCloseButton: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var triggerOnClickPersonalMovie by remember { mutableStateOf(false) }
    var openSharedLists by remember { mutableStateOf(false) }

    val userData by userViewModel.userData.collectAsState()
    val info by apiViewModel.movieInfo.collectAsState()
    val detailedInfo by apiViewModel.detailedInfo.collectAsState()

    val posterAlpha by animateFloatAsState(
        targetValue = (1f - scrollState.value / 600f).coerceIn(0f, 1f),
        animationSpec = tween(0),
        label = "posterAlpha"
    )

    val (animatedBg, animatedTitle, animatedAccent, animatedButtonBg) =
        rememberDynamicPaletteColors(imageUrl = movie.posterUrl)

    LaunchedEffect(triggerOnClickPersonalMovie) {
        if (triggerOnClickPersonalMovie) {
            personalViewModel.toastMessage.collect { resId ->
                showToast(context = context, messageId = resId)
                onCloseButton()
            }
        }
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }
    LaunchedEffect(movie.id) {
        apiViewModel.getSearchMovieById(movie.id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBg)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .graphicsLayer { alpha = posterAlpha }
            ) {
                // region Poster image
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.nameFilm,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // endregion

                Column {
                    // region Верхние кнопки
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(TopAppBarDefaults.TopAppBarExpandedHeight)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // region Кнопка Назад
                        OutlinedButton(
                            onClick = onCloseButton,
                            shape = RoundedCornerShape(23.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = animatedBg.copy(alpha = 0.9f),
                                contentColor = DynamicContentColor
                            ),
                            border = null,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(23.dp),
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                modifier = Modifier.padding(end = 10.dp),
                                text = stringResource(R.string.button_back),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        //endregion

                        // region Тип
                        Row(
                            modifier = Modifier
                                .background(
                                    color = animatedBg.copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(23.dp)
                                )
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                color = DynamicContentColor,
                                text = decoderTypeMovie(detailedInfo?.type),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        // endregion
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 14.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {

                        // region Кнопка Избранное
                        if (needFavoritesButton) {
                            OutlinedButton(
                                onClick = {
                                    personalViewModel.addMovie(userId, selectedMovie = movie)
                                    triggerOnClickPersonalMovie = true
                                },
                                shape = RoundedCornerShape(23.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = animatedBg.copy(alpha = 0.9f),
                                    contentColor = DynamicContentColor
                                ),
                                border = null,
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(26.dp),
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null
                                )
                            }
                        }
                        // endregion

                        // region Кнопка Просмотрен
                        if (needSkipButton) {
                            OutlinedButton(
                                onClick = { /*TODO: Действие*/ },
                                shape = RoundedCornerShape(23.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = animatedBg.copy(alpha = 0.9f),
                                    contentColor = DynamicContentColor
                                ),
                                border = null,
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(26.dp),
                                    imageVector = Icons.Default.RemoveRedEye,
                                    contentDescription = null
                                )
                            }
                        }
                        // endregion

                        // region Кнопка контроль серий
                        if (
                            detailedInfo?.type == "TV_SERIES" ||
                            detailedInfo?.type == "MINI_SERIES"
                        ) {
                            OutlinedButton(
                                onClick = {
                                    seriesViewModel.addNewEntry(userId, movie.nameFilm)
                                    showToast(context, R.string.Series_control_start)
                                },
                                shape = RoundedCornerShape(23.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = animatedBg.copy(alpha = 0.9f),
                                    contentColor = DynamicContentColor
                                ),
                                border = null,
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(37.dp),
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                            }
                        }
                        // endregion

                    }
                    // endregion
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(7.dp))
                RatingRow(movie = detailedInfo)

                // region Title
                Text(
                    text = movie.nameFilm,
                    style = MaterialTheme.typography.displayLarge,
                    color = animatedTitle,
                )
                // endregion

                Spacer(Modifier.height(20.dp))

                // region Кнопки
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ActionButton(
                        icon = Icons.Outlined.PostAdd,
                        label = context.getString(R.string.text_buttons_film_card_to_shared_list),
                        accentColor = animatedAccent,
                        borderColor = animatedAccent,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openSharedLists = true }
                    )
                    Spacer(Modifier.height(10.dp))
                    if (needReviews) {
                        ActionButton(
                            icon = Icons.Default.CommentBank,
                            label = context.getString(R.string.button_show_response),
                            accentColor = animatedAccent,
                            borderColor = animatedAccent,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { reviews() }
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    if (detailedInfo?.type == "TV_SERIES" || detailedInfo?.type == "MINI_SERIES") {
                        ActionButton(
                            icon = Icons.Default.PostAdd,
                            label = context.getString(R.string.button_open_waiting_list),
                            accentColor = animatedAccent,
                            borderColor = animatedAccent,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { sendToWaitingList() }
                        )

                    }
                }
                // endregion

                Spacer(Modifier.height(10.dp))
                ExpandedCard(
                    title = stringResource(R.string.text_for_expandedCard_field),
                    description = info?.description ?: stringResource(R.string.limit_is_over),
                    animatedAccent = animatedAccent,
                    contentColor = DynamicContentColor
                )
                Spacer(Modifier.height(24.dp))
            }
        }

        // region Переработать под новое оформление
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
                            userId = userId,
                            userName = user.nikName,
                            navController = navController,
                            addButton = true,
                            selectedMovie = movie,
                            onCloseSharedLists = {
                                openSharedLists = false
                                onCloseButton()
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun decoderTypeMovie(type: String?): String {
    type?.let {
        return when {
            it.contains("FILM") -> "Фильм"
            it.contains("TV_SHOW") -> "Передача"
            it.contains("TV_SERIES") -> "Сериал"
            it.contains("MINI_SERIES") -> "Сериал"
            it.contains("ALL") -> ""
            else -> "Тип не указан"
        }
    }
    return ""
}

@Composable
private fun RatingRow(movie: MovieData.MovieSearch?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = RatingBadgeColor
        ) {
            Text(
                text = "IMDB: ${movie?.ratingImdb ?: "Н/Д"}",
                color = Color.Black,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            )
        }
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = RatingBadgeColor
        ) {
            Text(
                text = "КП: ${movie?.ratingKinopoisk ?: "Н/Д"}",
                color = Color.Black,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            )
        }
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = RatingBadgeColor
        ) {
            Text(
                text = movie?.year ?: "Н/Д",
                color = Color.Black,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            )
        }
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = RatingBadgeColor
        ) {
            Text(
                text = "${movie?.filmLength ?: "Н/Д"} мин.",
                color = Color.Black,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.secondary,
    borderColor: Color = MaterialTheme.colorScheme.secondary,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = DynamicContentColor
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = SolidColor(borderColor)
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = accentColor
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}






//                    ActionButton(
//                        onClick2 = { skipButton() }
//                    )
//                    ActionButton(
//                        onClick2 = { sendToArchive() }
//                    )