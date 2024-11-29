package com.pozmaxpav.cinemaopinion.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ThemeRepositoryQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SystemSharedPreferencesRepositoryQualifier
