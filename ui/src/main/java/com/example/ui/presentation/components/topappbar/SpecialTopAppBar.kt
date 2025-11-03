package com.example.ui.presentation.components.topappbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialTopAppBar(
    isAtTop: Boolean,
    title: String,
    goToHome: () -> Unit,
    goToBack: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isAtTop) MaterialTheme.colorScheme.background else Color.Transparent,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
    )

    val offset by animateDpAsState(
        targetValue = if (isAtTop) 0.dp else 7.dp,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .height(TopAppBarDefaults.TopAppBarExpandedHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TopBarIconGroup(
            modifier = Modifier.graphicsLayer {
                translationX = offset.toPx()
                translationY = offset.toPx()
            },
            icons = listOf(Icons.Default.ArrowBackIosNew, Icons.Default.Home),
            color = MaterialTheme.colorScheme.background,
            tint = MaterialTheme.colorScheme.secondary,
            onClick1 = goToBack,
            onClick2 = goToHome
        )

        TopAppBarString(
            modifier = Modifier.graphicsLayer {
                translationX = -offset.toPx()
                translationY = offset.toPx()
            },
            title = title,
            color = MaterialTheme.colorScheme.background,
            textColor = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun TopAppBarString(
    modifier: Modifier = Modifier,
    title: String,
    color: Color,
    textColor: Color
) {
    Row(
        modifier = modifier
            .background(
                color = color.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium,
            color = textColor
        )
    }
}

@Composable
private fun TopBarIconGroup(
    modifier: Modifier = Modifier,
    icons: List<ImageVector>,
    color: Color,
    tint: Color,
    onClick1: () -> Unit,
    onClick2: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = color.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.clickable(onClick = onClick1),
            imageVector = icons[0],
            contentDescription = null,
            tint = tint
        )
        Spacer(Modifier.padding(horizontal = 5.dp))
        Icon(
            modifier = Modifier.clickable(onClick = onClick2),
            imageVector = icons[1],
            contentDescription = null,
            tint = tint
        )
    }
}

