package com.pozmaxpav.cinemaopinion.presentation.components.detailscards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.SharedListsScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.utilits.showToast

@Composable
fun DetailsCardSelectedMovie(
    movie: DomainSelectedMovieModel,
    userId: String = "",
    navController: NavHostController,
    reviews: @Composable () -> Unit = {},
    commentButton: @Composable () -> Unit = {},
    apiViewModel: ApiViewModel = hiltViewModel(),
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    onCloseButton: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var triggerOnClickPersonalMovie by remember { mutableStateOf(false) }
    var openSharedLists by remember { mutableStateOf(false) }

    val userData by userViewModel.userData.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()
    val detailedInfo by apiViewModel.detailedInformationAboutFilm.collectAsState()

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
    }
    LaunchedEffect(movie.id) {
        apiViewModel.getSearchMovieById(movie.id)
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            // region Верхние кнопки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCloseButton) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = {
                        personalMovieViewModel.addMovie(userId, selectedMovie = movie)
                        triggerOnClickPersonalMovie = true
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            // endregion
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .verticalScroll(scrollState)
            ) {
                DetailCardPoster(movie, detailedInfo)

                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.padding(15.dp))
                    Text(
                        text = movie.nameFilm,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.padding(vertical = 5.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ExpandedCard(
                            title = stringResource(R.string.text_for_expandedCard_field),
                            description = info?.description
                                ?: stringResource(R.string.limit_is_over)
                        )
                        Spacer(Modifier.padding(5.dp))
                        CustomTextButton(
                            textButton = context.getString(R.string.text_buttons_film_card_to_shared_list),
                            imageVector = Icons.Outlined.PostAdd,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.fillMaxWidth(),
                            onClickButton = { openSharedLists = true }
                        )
                        Spacer(Modifier.padding(5.dp))
                        commentButton()
                        Spacer(Modifier.padding(5.dp))
                        reviews()
                        Spacer(Modifier.padding(10.dp))
                    }
                }
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

@Composable
private fun DetailCardPoster(
    movie: DomainSelectedMovieModel,
    detailedInfo: MovieData.MovieSearch?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .width(220.dp)
                .aspectRatio(2f / 3f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.nameFilm,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ShowRating(movie = detailedInfo)
                }
            }
        }
    }
}

@Composable
private fun ShowRating(movie: MovieData.MovieSearch?) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(7.dp)
        ) {
            Text(
                text = "КП: ${movie?.ratingKinopoisk ?: "Н/Д"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "IMDB: ${movie?.ratingImdb ?: "Н/Д"}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

}