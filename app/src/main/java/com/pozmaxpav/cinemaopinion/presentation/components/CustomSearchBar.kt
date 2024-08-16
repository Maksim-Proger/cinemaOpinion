package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    searchHistory: List<String>
) {
    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 30.dp),
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (active) {
                IconButton(
                    onClick = { if (query.isNotEmpty()) onQueryChange("") else onActiveChange(false) }
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                }
            }
        }
    ) {
        // Здесь можно добавить контент, который будет отображаться под строкой поиска
        searchHistory.takeLast(3).forEach { item ->
            ListItem(
//                modifier = Modifier.clickable { query = item }, // TODO: Придумать как передать
                headlineContent = { Text(text = item) },
                leadingContent = {
                    Icon(painter = painterResource(id = R.drawable.ic_history), contentDescription = "History icon")
                }
            )
        }
    }
}
