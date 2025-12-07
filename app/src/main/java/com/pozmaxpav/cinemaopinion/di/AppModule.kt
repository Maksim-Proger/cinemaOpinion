package com.pozmaxpav.cinemaopinion.di

import android.content.Context
import com.example.core.utils.FirebaseListenerHolder
import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.data.api.MovieApi
import com.pozmaxpav.cinemaopinion.data.api.MovieInformationApi
import com.pozmaxpav.cinemaopinion.data.repository.api.GetMovieInformationApiRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.api.MovieRepositoryApiImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.MovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.PersonalMovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.NotificationRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.SeriesControlRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.SharedListsRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.UserRepoImpl
import com.pozmaxpav.cinemaopinion.data.repository.system.SystemRepositoryAppImpl
import com.pozmaxpav.cinemaopinion.domain.repository.api.GetMovieInformationApiRepository
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.NotificationRepository
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SeriesControlRepository
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepo
import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemRepositoryApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // region API

    @Provides
    @Singleton
    fun provideGetMovieListApi(): MovieApi {
        return Retrofit
            .Builder()
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().also {
                        it.level = HttpLoggingInterceptor.Level.BODY
                    }
                ).build()
            )
            .baseUrl("https://kinopoiskapiunofficial.tech")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<MovieApi>()
    }

    @Provides
    @Singleton
    fun provideMovieRepositoryApi(api: MovieApi): MovieRepositoryApi {
        return MovieRepositoryApiImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetMovieInformationApi(): MovieInformationApi {
        return Retrofit
            .Builder()
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().also {
                        it.level = HttpLoggingInterceptor.Level.BODY
                    }
                ).build()
            )
            .baseUrl("https://api.kinopoisk.dev")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<MovieInformationApi>()
    }

    @Provides
    @Singleton
    fun provideGetMovieInformationApiRepository(api: MovieInformationApi): GetMovieInformationApiRepository {
        return GetMovieInformationApiRepositoryImpl(api)
    }

    // endregion

    // region Firebase

    @Provides
    @Singleton
    fun provideMovieRepository(
        databaseReference: DatabaseReference,
        listenerHolder: FirebaseListenerHolder
    ): MovieRepository {
        return MovieRepositoryImpl(databaseReference, listenerHolder)
    }

    @Provides
    @Singleton
    fun provideSeriesControlRepository(
        databaseReference: DatabaseReference,
        listenerHolder: FirebaseListenerHolder
    ): SeriesControlRepository {
        return SeriesControlRepositoryImpl(databaseReference, listenerHolder)
    }

    @Provides
    @Singleton
    fun providePersonalMovieRepository(
        databaseReference: DatabaseReference,
        listenerHolder: FirebaseListenerHolder
    ): PersonalMovieRepository {
        return PersonalMovieRepositoryImpl(databaseReference, listenerHolder)
    }

    @Provides
    @Singleton
    fun provideRecordsOfChangesRepository(
        databaseReference: DatabaseReference
    ): NotificationRepository {
        return NotificationRepositoryImpl(databaseReference)
    }

    @Provides
    @Singleton
    fun provideSharedListsRepository(
        databaseReference: DatabaseReference
    ): SharedListsRepository {
        return SharedListsRepositoryImpl(databaseReference)
    }

    @Provides
    @Singleton
    fun provideUserRepo(
        databaseReference: DatabaseReference
    ) : UserRepo {
        return UserRepoImpl(databaseReference)
    }

    // endregion

    // region System

    @Provides
    @Singleton
    fun provideSystemRepositoryApp(@ApplicationContext context: Context): SystemRepositoryApp {
        return SystemRepositoryAppImpl(context)
    }

    // endregion

}
