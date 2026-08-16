package com.pozmaxpav.cinemaopinion.presentation.components.detailscards

import android.widget.RatingBar
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
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
import coil.compose.AsyncImage
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.ExpandedCard
import com.example.ui.presentation.components.ratingbar.RatingBarScaffold
import com.example.ui.presentation.theme.DynamicContentColor
import com.example.ui.presentation.theme.RatingBadgeColor
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.utilities.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsCardSpecial(
    movie: DomainSelectedMovieModel,
    userId: String,
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
    onCloseButton: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var triggerOnClickPersonalMovie by remember { mutableStateOf(false) }

    val info by apiViewModel.movieInfo.collectAsState()
    val detailedInfo by apiViewModel.detailedInfo.collectAsState()
    var showRatingBar by remember { mutableStateOf(false) }
    val quantitySeasonalEventPoints by userViewModel.seasonalEventPoints.collectAsState()

    val (animatedBg, animatedTitle, animatedAccent, animatedButtonBg) =
        rememberDynamicPaletteColors(imageUrl = movie.posterUrl)

    LaunchedEffect(triggerOnClickPersonalMovie) {
        if (triggerOnClickPersonalMovie) {
            personalMovieViewModel.toastMessage.collect { resId ->
                showToast(context = context, messageId = resId)
                onCloseButton()
            }
        }
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
        userViewModel.getSeasonalEventPoints(userId)
    }
    LaunchedEffect(movie.id) {
        apiViewModel.getInformationMovie(movie.id)
    }

    val posterAlpha by animateFloatAsState(
        targetValue = (1f - scrollState.value / 600f).coerceIn(0f, 1f),
        animationSpec = tween(0),
        label = "posterAlpha"
    )

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
                // region Poster
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // endregion
                // region Up Button's
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(TopAppBarDefaults.TopAppBarExpandedHeight)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // region Button Back
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
                    // endregion

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // region Кнопка Избранное
                        OutlinedButton(
                            onClick = {
                                personalMovieViewModel.addMovie(userId, movie)
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
                                imageVector = Icons.Default.Favorite,
                                modifier = Modifier.size(26.dp),
                                contentDescription = null
                            )
                        }
                        // endregion
                        // region Кнопка Просмотрен
                        OutlinedButton(
                            onClick = {
                                userViewModel.updatingEventData(userId)
                                showRatingBar = !showRatingBar
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
                                imageVector = Icons.Default.RemoveRedEye,
                                contentDescription = null
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
                    text = movie.nameFilm,
                    style = MaterialTheme.typography.displayLarge,
                    color = animatedTitle
                )
                // endregion

                Spacer(Modifier.height(20.dp))

                // region Button's
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
                        onClick = {  }
                    )
                }
                // endregion

                Spacer(Modifier.height(20.dp))
                ExpandedCard(
                    title = stringResource(R.string.text_for_expandedCard_field),
                    description = info?.description ?: stringResource(R.string.limit_is_over),
                    animatedAccent = animatedAccent,
                    contentColor = DynamicContentColor
                )
                Spacer(Modifier.height(24.dp))
            }
        }

        // region RatingBar
        if (showRatingBar) {
            RatingBarScaffold(
                score = quantitySeasonalEventPoints,
                onCloseButton = {
                    showRatingBar = !showRatingBar
                }
            )
            BackHandler {
                showRatingBar = false
            }
        }
        // endregion

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
                text = "IMDB: ${movie?.ratingKinopoisk ?: "Н/Д"}",
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
    onClick: () -> Unit = {},
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








////                        ExpandedCard(
////                            title = stringResource(R.string.text_for_expandedCard_field),
////                            description = info?.description
////                                ?: stringResource(R.string.limit_is_over)
////                        )
