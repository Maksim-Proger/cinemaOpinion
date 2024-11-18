package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.news.NewsModel
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.formatDate
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isFirstLoad by remember { mutableStateOf(true) } // Флаг для предотвращения повторных запусков

    LaunchedEffect(Unit) { // Первоначальный запрос
        if (isFirstLoad) {
            viewModel.getMediaNews(currentPage)
            isFirstLoad = false
        }
    }

    LaunchedEffect(mediaNewsList.value?.totalPages) { // Основной запрос с последней страницы
        val totalPages = mediaNewsList.value?.totalPages
        if (totalPages != null && totalPages > 0 && currentPage == 1) {
            currentPage = totalPages
            viewModel.getMediaNews(currentPage)
        }
//        Log.d("@@@", totalPages.toString()) // TODO: Разобраться почему изначально выводится значение null
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ClassicTopAppBar(
                title = "Медиа новости",
                scrollBehavior = scrollBehavior,
                onTransitionAction = { navigateFunction(navController, Route.MainScreen.route) }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(5.dp)
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
        HorizontalDivider(Modifier.padding(vertical = 7.dp))

        Text(
            text = item.description ?: "Нет описания",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.padding(vertical = 5.dp))
        Text(
            text = item.url ?: "Нет источника",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Blue
        )

        HorizontalDivider(Modifier.padding(vertical = 7.dp))
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

