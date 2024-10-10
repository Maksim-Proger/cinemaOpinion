package com.example.introductoryscreens.ui.onboarding

import androidx.annotation.DrawableRes
import com.example.introductoryscreens.R


data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int // TODO что это за аннотация?
)

// Список страниц вступительных экранов
val pages = listOf(
    Page(
        "Не пропускайте хорошие фильмы!",
        "Создавайте личный список и добавляйте туда фильмы которые хотите посмотреть.",
        R.drawable.onboarding1
    ),
    Page(
        "Делитесь хорошим кино с вашей семьёй!",
        "Пополняйте общий список с фильмами, чтобы и ваши родные могли расслабиться.",
        R.drawable.onboarding2
    ),
    Page(
        "Никогда не переставайте отрываться!",
        "Хороший фильм, залог хорошего вечера.",
        R.drawable.onboarding3
    ),
)

