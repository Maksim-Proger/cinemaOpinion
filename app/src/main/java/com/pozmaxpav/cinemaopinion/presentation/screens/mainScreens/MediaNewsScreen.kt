package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.news.NewsModel
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.formatDate
import com.pozmaxpav.cinemaopinion.utilits.returnToMainScreen

// TODO: Доработать! Надо запускать с последней страницы
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaNewsScreen(
    navController: NavHostController,
) {
    val viewModel: MainViewModel = hiltViewModel()
    val mediaNewsList = viewModel.mediaNews.collectAsState()
    val newsToDisplay: List<NewsModel> = mediaNewsList.value?.items ?: emptyList()
    var currentPage by remember { mutableIntStateOf(1) }
    var showPageSwitchingButtons by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getMediaNews(currentPage)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Медиа новости",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    IconButton(onClick = {
                        returnToMainScreen(navController, Route.MainScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = stringResource(id = R.string.description_icon_home_button),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(newsToDisplay, key = { it.kinopoiskId }) { it ->
                NewsItem(it)
            }
        }
    }
}

@Composable
fun NewsItem(
    item: NewsModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.title ?: "Нет заголовка",
                style = MaterialTheme.typography.displayLarge
            )
        }
        HorizontalDivider()

        Text(
            text = item.description ?: "Нет описания",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = item.url ?: "Нет источника",
            style = MaterialTheme.typography.bodySmall
        )

        HorizontalDivider()
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            if (item.publishedAt != null) {
                val dateTime = formatDate(item.publishedAt.toString())
                Text(
                    text = dateTime,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    text = "Нет даты публикации",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

