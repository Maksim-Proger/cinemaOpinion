package com.example.intro.di

import android.app.Application
import com.example.intro.data.IntroScreensRepositoryImpl
import com.example.intro.domain.repository.IntroScreensRepository
import com.example.intro.domain.usecases.ReadAppEntryUseCase
import com.example.intro.domain.usecases.SaveAppEntryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IntroModule {

    // region IntroductionScreens

    @Provides
    @Singleton
    fun provideLocalUserManager(application: Application): IntroScreensRepository =
        IntroScreensRepositoryImpl(application)

    // endregion

}