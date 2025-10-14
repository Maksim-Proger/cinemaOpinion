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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
        inputField = {
            SearchBarDefaults.InputField(
                modifier = Modifier.fillMaxWidth(),
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                expanded = active,
                onExpandedChange = onActiveChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                },
                trailingIcon = {
                    if (active) {
                        IconButton(
                            onClick = {
                                if (query.isNotEmpty()) onQueryChange("")
                                else onActiveChange(false)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        expanded = active,
        onExpandedChange = onActiveChange,
        modifier = Modifier.fillMaxWidth().padding(7.dp),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        searchHistory.takeLast(5).forEach { item ->
            ListItem(
                modifier = Modifier.clickable {
                    onQueryChange(item)
                    onSearch(item)
                },
                headlineContent = {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_history),
                        contentDescription = "History icon"
                    )
                }
            )
        }
    }
}

