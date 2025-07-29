package com.pozmaxpav.cinemaopinion.presentation.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel

@Composable
fun SharedListItem(
    item: DomainSharedListModel,
    onClick: () -> Unit
) {
    Card (
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Column(
            modifier = Modifier.padding(7.dp)
        ) {
            Text(text = item.title)
            Text(text = item.source)
            Text(text = item.users)
        }
    }
}