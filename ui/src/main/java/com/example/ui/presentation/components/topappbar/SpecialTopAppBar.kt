package com.example.ui.presentation.components.topappbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.presentation.components.dropmenu.DropdownMenuItem
import com.example.ui.presentation.components.dropmenu.SettingsMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialTopAppBar(
    isAtTop: Boolean,
    title: String,
    goToBack: () -> Unit,
    onArchiveClick: () -> Unit = {},
    onWaitlistClick: () -> Unit = {}
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
            .wrapContentHeight()
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
            color = MaterialTheme.colorScheme.secondaryContainer,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick1 = goToBack
        )

        Row(
            modifier = Modifier.wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopAppBarString(
                modifier = Modifier.graphicsLayer {
                    translationX = -offset.toPx()
                    translationY = offset.toPx()
                },
                title = title,
                color = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )

            if (
                title != stringResource(R.string.title_page_personal_list) &&
                !title.contains(stringResource(R.string.button_open_waiting_list_screen)) &&
                !title.contains(stringResource(R.string.button_open_archive_screen))
            ) {

                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Menu(
                    modifier = Modifier.graphicsLayer {
                        translationX = -offset.toPx()
                        translationY = offset.toPx()
                    },
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    onArchiveClick = onArchiveClick,
                    onWaitlistClick = onWaitlistClick
                )
            }
        }
    }
}

@Composable
private fun Menu(
    modifier: Modifier,
    color: Color,
    tint: Color,
    onArchiveClick: () -> Unit = {},
    onWaitlistClick: () -> Unit = {}
) {
    SettingsMenu(
        modifier = modifier
            .background(
                color = color.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 10.dp),
        tint = tint
    ) { closeMenu ->
        DropdownMenuItem(
            onAction = {
                onArchiveClick()
                closeMenu()
            },
            title = stringResource(R.string.button_open_archive_screen),
            leadingIcon = null
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
        DropdownMenuItem(
            onAction = {
                onWaitlistClick()
                closeMenu()
            },
            title = stringResource(R.string.button_open_waiting_list_screen),
            leadingIcon = null
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
                shape = RoundedCornerShape(23.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
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
    textColor: Color,
    onClick1: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onClick1,
            shape = RoundedCornerShape(23.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = color.copy(alpha = 0.8f),
                contentColor = textColor
            ),
            border = null,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = icons[0],
                contentDescription = null,
                tint = tint
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "Назад",
                style = MaterialTheme.typography.displayMedium,
                color = textColor
            )
        }
    }
}

