package com.pozmaxpav.cinemaopinion.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.presentation.components.SearchBar
import com.pozmaxpav.cinemaopinion.presentation.components.TopAppBar
import com.pozmaxpav.cinemaopinion.ui.theme.CinemaOpinionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var query by remember { mutableStateOf("") }
    var searchBarActive by remember { mutableStateOf(false) }
    var onAccountButtonClick by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                onSearchButtonClick = { searchBarActive = !searchBarActive },
                onAccountButtonClick = { onAccountButtonClick = true },
                scrollBehavior = scrollBehavior
            )

            if (onAccountButtonClick) {
                AccountScreen(
                    onClick = { onAccountButtonClick = false } // Закрытие диалога при нажатии
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding)
        ) {
            if (searchBarActive) {
                SearchBar(
                    query = query,
                    onQueryChange = {newQuery -> query = newQuery},
                    onSearch = {searchQuery -> /* обработка поиска */},
                    active = searchBarActive,
                    onActiveChange = {isActive -> searchBarActive = isActive}
                )
            }
        }

        /**
         * Этот код использует компонент SearchBar внутри Column, который является контейнером для вертикального размещения элементов.
         * Вот что происходит:
         *
         * modifier = Modifier.padding(padding): Добавляет отступы вокруг Column, используя значение padding, которое передается из Scaffold.
         * Это отступы, которые обеспечивают корректное размещение элементов внутри Column, учитывая отступы от Scaffold.
         *
         * if (searchBarActive): Условие для проверки, активен ли поиск (searchBarActive).
         * Если searchBarActive равно true, то компонент SearchBar будет отображаться. В противном случае, он не будет отображаться.
         *
         * Передача параметров в SearchBar:
         *
         * query = query: Передает текущее значение запроса.
         * onQueryChange = { newQuery -> query = newQuery }: Обновляет состояние query при изменении текста в поле поиска.
         * onSearch = { searchQuery -> /* обработка поиска */ }: Позволяет обработать поиск, когда пользователь выполняет поиск.
         * active = searchBarActive: Управляет состоянием активности SearchBar.
         * onActiveChange = { isActive -> searchBarActive = isActive }: Обновляет состояние активности поиска.
         */

        if (!searchBarActive) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(30) {
                    ListItem(headlineContent = { Text(text = "item $it") }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CinemaOpinionTheme {
        MainScreen()
    }
}