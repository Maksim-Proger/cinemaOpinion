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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                onSearchButtonClick = { searchBarActive = !searchBarActive },
                scrollBehavior = scrollBehavior
            )
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