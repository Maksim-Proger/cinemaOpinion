package com.example.auth.di

import android.content.Context
import com.example.auth.data.repository.system.SystemRepositoryAuthImpl
import com.example.auth.data.repository.firebase.AuthRepositoryImplRepository
import com.example.auth.data.repository.system.UserPreferencesImpl
import com.example.auth.domain.repository.system.SystemRepositoryAuth
import com.example.auth.domain.repository.firebase.AuthRepository
import com.example.auth.domain.repository.system.UserPreferences
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    // region UserPreferences

    @Provides
    @Singleton
    fun provideUserPreferences(systemRepositoryAuth: SystemRepositoryAuth): UserPreferences {
        return UserPreferencesImpl(systemRepositoryAuth)
    }

    // endregion

    @Provides
    @Singleton
    fun provideSystemRepositoryAuth(@ApplicationContext context: Context): SystemRepositoryAuth {
        return SystemRepositoryAuthImpl(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(databaseReference: DatabaseReference): AuthRepository {
        return AuthRepositoryImplRepository(databaseReference)
    }



}