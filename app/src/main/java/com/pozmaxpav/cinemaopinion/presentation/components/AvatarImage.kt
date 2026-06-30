package com.pozmaxpav.cinemaopinion.presentation.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.backend.BackendApiProvider
import com.pozmaxpav.cinemaopinion.R

@Composable
fun AvatarImage(
    userId: String,
    modifier: Modifier = Modifier,
    previewUri: Uri? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    val model = if (previewUri != null) {
        previewUri
    } else {
        val url = BackendApiProvider.avatarUrl(userId)
        ImageRequest.Builder(context)
            .data(url)
            .memoryCacheKey(url)
            .diskCacheKey(url)
            .crossfade(false)
            .build()
    }

    SubcomposeAsyncImage(
        model = model,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier,
        loading = { AvatarPlaceholder() },
        error = { AvatarPlaceholder() }
    )
}

@Composable
private fun AvatarPlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.no_avatar_ic),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize()
    )
}
