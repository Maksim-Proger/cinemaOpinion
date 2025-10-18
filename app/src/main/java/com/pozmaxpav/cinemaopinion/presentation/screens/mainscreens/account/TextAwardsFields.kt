package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R

@Composable
fun TextAwardsFields(listAwards: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column {
            Text(
                text = stringResource(R.string.title_awards_field),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
            if (listAwards.isEmpty()) {
                Text(
                    text = stringResource(R.string.list_awards_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
    }
}