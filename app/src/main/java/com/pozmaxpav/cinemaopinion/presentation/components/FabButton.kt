package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pozmaxpav.cinemaopinion.R

@Composable
fun FabButton() {
    ExtendedFloatingActionButton(
        onClick = { /*TODO*/ },
        icon = { Icon(imageVector =Icons.Default.Add, contentDescription = null /*TODO: добавить описание*/) },
        text = { Text(text = stringResource(id = R.string.floating_action_button))}
    )
}