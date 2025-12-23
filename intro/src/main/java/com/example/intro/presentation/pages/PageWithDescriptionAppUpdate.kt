package com.example.intro.presentation.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.core.utils.CoreConstants.DEVELOPER_COMMENT
import com.example.intro.R
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.topappbar.TopAppBarAllScreens

// TODO: Надо доработать это, чтобы не приходилось напрямую связывать с app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageDescription(onDismiss: () -> Unit) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBarAllScreens(
                context = context,
                titleId = R.string.title_page_description,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        WithoutEventText(innerPadding, scrollState, onDismiss)
    }
}

@Composable
private fun WithoutEventText(
    innerPadding: PaddingValues,
    scrollState: ScrollState,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 25.dp, vertical = 7.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = DEVELOPER_COMMENT,
                style = MaterialTheme.typography.displayMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextButton(
                textButton = stringResource(R.string.button_to_begin),
                textStyle = MaterialTheme.typography.displayLarge,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier,
                onClickButton = onDismiss
            )
        }
    }
}

