package com.example.auth.di

import com.example.auth.data.repository.firebase.AuthRepositoryImplRepository
import com.example.auth.domain.repository.AuthRepository
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(databaseReference: DatabaseReference): AuthRepository {
        return AuthRepositoryImplRepository(databaseReference)
    }
}

