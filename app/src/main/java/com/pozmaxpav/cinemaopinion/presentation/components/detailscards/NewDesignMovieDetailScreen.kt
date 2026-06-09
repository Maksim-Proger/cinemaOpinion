package com.pozmaxpav.cinemaopinion.presentation.components.detailscards

import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.outlined.Description
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.ui.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.SharedListsScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.utilities.toSelectedMovie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDesignMovieDetailScreen(
    movie: MovieData?,
    userId: String,
    navController: NavHostController,
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    onCloseButton: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val detailedInfo by apiViewModel.detailedInfo.collectAsState()
    val info by apiViewModel.movieInfo.collectAsState()
    var openSharedLists by remember { mutableStateOf(false) }
    val userData by userViewModel.userData.collectAsState()

    // region Colors
    val background = MaterialTheme.colorScheme.background
    var dominantColor by remember { mutableStateOf(background) }
    val title = MaterialTheme.colorScheme.secondary
    var titleColor by remember { mutableStateOf(title) }
    var accentColor by remember { mutableStateOf(title) }
    val button = MaterialTheme.colorScheme.secondary
    var buttonBgColor by remember { mutableStateOf(button) }

    val animatedBg by animateColorAsState(dominantColor, label = "bg")
    val animatedTitle by animateColorAsState(titleColor, label = "title")
    val animatedAccent by animateColorAsState(accentColor, label = "accent")
    val animatedButtonBg by animateColorAsState(buttonBgColor, label = "buttonBg")

    LaunchedEffect(movie?.posterUrl) {
        val request = ImageRequest.Builder(context)
            .data(movie?.posterUrl)
            .allowHardware(false)
            .build()

        val result = context.imageLoader.execute(request)
        val bitmap = (result.drawable as? BitmapDrawable)?.bitmap

        bitmap?.let {
            Palette.from(it).generate { palette ->
                palette?.let { p ->
                    val raw = p.getDominantColor(background.toArgb())

                    // Ограничиваем яркость — не больше 20%
                    val hsl = FloatArray(3)
                    ColorUtils.colorToHSL(raw, hsl)
                    hsl[2] = hsl[2].coerceAtMost(0.2f)
                    dominantColor = Color(ColorUtils.HSLToColor(hsl))

                    titleColor = Color(p.getLightVibrantColor(title.toArgb()))
                    accentColor = Color(p.getVibrantColor(title.toArgb()))
                    buttonBgColor = Color(p.getDarkMutedColor(button.toArgb()))
                }
            }
        }
    }
    // endregion

    LaunchedEffect(movie?.id) {
        movie?.let { apiViewModel.getInformationMovie(it.id) }
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }
    LaunchedEffect(movie?.id) {
        movie?.let { apiViewModel.getSearchMovieById(it.id) }
    }

    // Parallax: poster fades as user scrolls
    val posterAlpha by animateFloatAsState(
        targetValue = (1f - scrollState.value / 600f).coerceIn(0f, 1f),
        animationSpec = tween(0),
        label = "posterAlpha"
    )

    // region Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .background(animatedBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Poster
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .graphicsLayer { alpha = posterAlpha }
            ) {
                // Poster image
                AsyncImage(
                    model = movie?.posterUrl,
                    contentDescription = movie?.nameRu ?: "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

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
                            containerColor = animatedButtonBg.copy(alpha = 0.9f),
                            contentColor = MaterialTheme.colorScheme.surface
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

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // region Кнопка Избранное
                        OutlinedButton(
                            onClick = {/*TODO: Действие*/ },
                            shape = RoundedCornerShape(23.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = animatedButtonBg.copy(alpha = 0.9f)
                            ),
                            border = null,
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(26.dp),
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        // endregion

                        // region Кнопка Просмотрен
                        OutlinedButton(
                            onClick = {/*TODO: Действие*/ },
                            shape = RoundedCornerShape(23.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = animatedButtonBg.copy(alpha = 0.9f)
                            ),
                            border = null,
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(26.dp),
                                imageVector = Icons.Default.RemoveRedEye,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        // endregion
                    }
                }
                // endregion
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
                    text = movie?.nameRu ?: "Нет названия",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = animatedTitle,
                    letterSpacing = 1.5.sp,
                )
                // endregion

                Spacer(Modifier.height(20.dp))

                // region Кнопки
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ActionButton(
                        icon = Icons.Outlined.PostAdd,
                        label = stringResource(R.string.text_buttons_recommend_film),
                        accentColor = animatedAccent,
                        borderColor = animatedAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { openSharedLists = true }
                    )
                    ActionButton(
                        icon = Icons.Default.CommentBank,
                        label = "Отзывы",
                        accentColor = animatedAccent,
                        borderColor = animatedAccent,
                        modifier = Modifier.weight(1f),
                        onClick = {/*TODO: Действие*/ }
                    )
                }
                // endregion

                Spacer(Modifier.height(20.dp))
                ExpandedCard(
                    title = stringResource(R.string.text_for_expandedCard_field),
                    description = info?.description ?: stringResource(R.string.limit_is_over),
                    animatedAccent = animatedAccent
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
    // endregion

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
                        selectedMovie = movie?.toSelectedMovie(),
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

@Composable
private fun RatingRow(
    movie: MovieData.MovieSearch?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFF5C518)
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
            color = Color(0xFFF5C518)
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
            color = Color(0xFFF5C518)
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
            color = Color(0xFFF5C518)
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
    accentColor: Color = MaterialTheme.colorScheme.secondary,   // ← новый параметр
    borderColor: Color = MaterialTheme.colorScheme.secondary,  // ← новый параметр
    onClick: () -> Unit = {},
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.surface
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



