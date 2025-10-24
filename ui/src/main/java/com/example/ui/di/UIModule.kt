package com.example.ui.di

import android.content.Context
import com.example.ui.data.repository.ThemeRepositoryImpl
import com.example.ui.domain.repositories.ThemeRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UIModule {
    // region SharedPreferences
    @Provides
    @Singleton
    fun provideThemeRepository(@ApplicationContext context: Context): ThemeRepository {
        return ThemeRepositoryImpl(context)
    }
    // endregion
}