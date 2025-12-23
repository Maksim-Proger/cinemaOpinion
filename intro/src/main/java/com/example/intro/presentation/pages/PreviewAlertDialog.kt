package com.example.intro.presentation.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.utils.CoreConstants.GENERAL_TEXT_FOR_ALERT_DIALOG
import com.example.core.utils.CoreConstants.NEW_YEAR_TEXT_FOR_ALERT_DIALOG
import com.example.ui.presentation.components.CustomTextButton

@Composable
fun PreviewAlertDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
//            WithoutEventButton(onDismiss)
            WithEventButton(onDismiss)
        },
        text = {
//            WithoutEventText()
            WithEventText()
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}

@Composable
private fun WithoutEventText() {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "\uD83D\uDC4B",
                fontSize = 65.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = GENERAL_TEXT_FOR_ALERT_DIALOG,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun WithoutEventButton(onDismiss: () -> Unit) {
    CustomTextButton(
        textButton = "Отлично!",
        textStyle = MaterialTheme.typography.labelMedium,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier,
        onClickButton = onDismiss
    )
}

@Composable
private fun WithEventText() {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "\u2744",
                fontSize = 65.sp
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = NEW_YEAR_TEXT_FOR_ALERT_DIALOG,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
@Composable
private fun WithEventButton(onDismiss: () -> Unit) {
    CustomTextButton(
        textButton = "Ура!!!! \uD83C\uDF89",
        textStyle = MaterialTheme.typography.labelMedium,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier,
        onClickButton = onDismiss
    )
}

