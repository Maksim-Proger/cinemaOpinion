package com.pozmaxpav.cinemaopinion.presentation.components.items

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel

@Composable
fun SharedListItem(
    item: DomainSharedListModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color = Color.Blue)
            .padding(vertical = 12.dp, horizontal = 12.dp)
            .clickable { onClick() },
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.padding(vertical = 5.dp))
        expandedCardTest(item.users)
    }
}

@Composable
private fun expandedCardTest(listParticipants: String) {

    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f,
        label = stringResource(R.string.animation_for_rotating_icon)
    )

    Column(
        modifier = Modifier
            .clickable(onClick = { expandedState = !expandedState })
            .animateContentSize( // Анимация для изменения размера карточки при раскрытии
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing // Эффект анимации (замедление к концу)
                )
            )
            .background(color = Color.Red)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { expandedState = !expandedState }
        ) {
            Text(
                text = "Участники",
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
