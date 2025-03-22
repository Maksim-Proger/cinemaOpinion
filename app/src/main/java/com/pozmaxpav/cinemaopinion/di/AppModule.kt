package com.pozmaxpav.cinemaopinion.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.introductoryscreens.domain.LocalUserManager
import com.example.introductoryscreens.domain.usecases.AppEntryUseCases
import com.example.introductoryscreens.domain.usecases.ReadAppEntry
import com.example.introductoryscreens.domain.usecases.SaveAppEntry
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pozmaxpav.cinemaopinion.data.api.GetMovieInformationApi
import com.pozmaxpav.cinemaopinion.data.api.MovieListApi
import com.pozmaxpav.cinemaopinion.data.db.datastore.LocalUserManagerImpl
import com.pozmaxpav.cinemaopinion.data.repository.api.GetMovieInformationApiRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.api.MovieRepositoryApiImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.MovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.RecordsOfChangesRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.PersonalMovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.SeriesControlRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.firebase.UserRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.system.SharedPreferencesRepository
import com.pozmaxpav.cinemaopinion.domain.repository.api.GetMovieInformationApiRepository
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepositoryApi
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.RecordsOfChangesRepository
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SeriesControlRepository
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepository
import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemSharedPreferencesRepository
import com.pozmaxpav.cinemaopinion.domain.repository.system.ThemeRepository
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
    fun provideGetMovieListApi() : MovieListApi {
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
            .create<MovieListApi>()
    }

    @Provides
    @Singleton
    fun provideMovieRepositoryApi(api: MovieListApi): MovieRepositoryApi {
        return MovieRepositoryApiImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetMovieInformationApi(): GetMovieInformationApi {
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
            .create<GetMovieInformationApi>()
    }

    @Provides
    @Singleton
    fun provideGetMovieInformationApiRepository(api: GetMovieInformationApi):
            GetMovieInformationApiRepository {
        return GetMovieInformationApiRepositoryImpl(api)
    }

    // endregion

    // region SharedPreferences

    @Provides
    @Singleton
    @ThemeRepositoryQualifier
    fun provideThemeRepository(@ApplicationContext context: Context): ThemeRepository {
        return SharedPreferencesRepository(context)
    }

    @Provides
    @Singleton
    @SystemSharedPreferencesRepositoryQualifier
    fun provideSystemSharedPreferencesRepository(@ApplicationContext context: Context): SystemSharedPreferencesRepository {
        return SharedPreferencesRepository(context)
    }

    // endregion

    // region Room

//    @Provides
//    @Singleton
//    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(
//            context,
//            AppDatabase::class.java,
//            "user_database"
//        ).build()
//    }

    // endregion

    // region Firebase

    @Provides
    @Singleton
    fun provideFirebaseDataBase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideDatabaseReference(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference // Создание DatabaseReference
    }

    @Provides
    @Singleton
    fun provideMovieRepository(databaseReference: DatabaseReference): MovieRepository {
        return MovieRepositoryImpl(databaseReference) // Передача DatabaseReference в репозиторий
    }

    @Provides
    @Singleton
    fun provideSeriesControlRepository(databaseReference: DatabaseReference): SeriesControlRepository {
        return SeriesControlRepositoryImpl(databaseReference) // Передача DatabaseReference в репозиторий
    }

    @Provides
    @Singleton
    fun providePersonalMovieRepository(databaseReference: DatabaseReference): PersonalMovieRepository {
        return PersonalMovieRepositoryImpl(databaseReference) // Передача DatabaseReference в репозиторий
    }

    @Provides
    @Singleton
    fun provideUserRepository(databaseReference: DatabaseReference): UserRepository {
        return UserRepositoryImpl(databaseReference) // Передача DatabaseReference в репозиторий
    }

    @Provides
    @Singleton
    fun provideRecordsOfChangesRepository(databaseReference: DatabaseReference): RecordsOfChangesRepository {
        return RecordsOfChangesRepositoryImpl(databaseReference) // Передача DatabaseReference в репозиторий
    }

    // endregion

    // region IntroductionScreens

    @Provides
    @Singleton
    fun provideLocalUserManager(application: Application): LocalUserManager =
        LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(localUserManager: LocalUserManager) =
        AppEntryUseCases(
            readAppEntry = ReadAppEntry(localUserManager),
            saveAppEntry = SaveAppEntry(localUserManager)
        )

    // endregion

}
