package com.pozmaxpav.cinemaopinion.presentation.components.items

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel

@Composable
fun SharedListItem(
    item: DomainSharedListModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = 12.dp, horizontal = 12.dp)
            .clickable { onClick() },
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.padding(vertical = 5.dp))
        ExpandedCardTest(item.users)
    }
}

@Composable
private fun ExpandedCardTest(listParticipants: String) {

    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f,
        label = stringResource(R.string.animation_for_rotating_icon)
    )

    Column(
        modifier = Modifier
            .clickable(onClick = { expandedState = !expandedState })
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { expandedState = !expandedState }
        ) {
            Text(
                text = stringResource(R.string.list_participants),
                style = MaterialTheme.typography.bodySmall
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(18.dp)
                    .rotate(rotationState)
            )
        }

        if (expandedState) {
            Spacer(Modifier.padding(vertical = 5.dp))
            Column {
                Text(
                    text = listParticipants,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
