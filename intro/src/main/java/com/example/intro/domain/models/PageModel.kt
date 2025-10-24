package com.example.intro.domain.models

import androidx.annotation.DrawableRes

data class PageModel(
    val title: String,
    val description: String,
    @param:DrawableRes val image: Int
)
