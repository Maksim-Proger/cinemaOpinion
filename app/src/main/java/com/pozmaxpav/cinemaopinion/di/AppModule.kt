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
import com.pozmaxpav.cinemaopinion.data.local.datastore.LocalUserManagerImpl
import com.pozmaxpav.cinemaopinion.data.local.room.appdb.AppDatabase
import com.pozmaxpav.cinemaopinion.data.local.room.dao.CommentPersonalListDao
import com.pozmaxpav.cinemaopinion.data.local.room.dao.SelectedMovieDao
import com.pozmaxpav.cinemaopinion.data.repository.CommentPersonalListRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.GetMovieInformationApiRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.MovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.SelectedMovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.SharedPreferencesRepository
import com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase.FirebaseRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase.SeriesControlRepositoryImpl
import com.pozmaxpav.cinemaopinion.domain.repository.remote.CommentPersonalListRepository
import com.pozmaxpav.cinemaopinion.domain.repository.api.GetMovieInformationApiRepository
import com.pozmaxpav.cinemaopinion.domain.repository.api.MovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SelectedMovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemSharedPreferencesRepository
import com.pozmaxpav.cinemaopinion.domain.repository.system.ThemeRepository
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SeriesControlRepository
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
    fun provideMovieRepository(api: MovieListApi): MovieRepository {
        return MovieRepositoryImpl(api)
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

    // region Shared Preferences

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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "user_database"
        ).build()
    }

    // endregion

    // region SelectedFilm

    @Provides
    @Singleton
    fun provideSelectedMovieDao(appDatabase: AppDatabase): SelectedMovieDao {
        return appDatabase.selectedMovieDao()
    }

    @Provides
    @Singleton
    fun provideSelectedMovieRepository(selectedMovieDao: SelectedMovieDao): SelectedMovieRepository {
        return SelectedMovieRepositoryImpl(selectedMovieDao)
    }

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
    fun provideFirebaseRepository(databaseReference: DatabaseReference): FirebaseRepository {
        return FirebaseRepositoryImpl(databaseReference) // Передача DatabaseReference в репозиторий
    }

    @Provides
    @Singleton
    fun provideSeriesControlRepository(databaseReference: DatabaseReference): SeriesControlRepository {
        return SeriesControlRepositoryImpl(databaseReference) // Передача DatabaseReference в репозиторий
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

    // region CommentPersonalList

    @Provides
    fun provideCommentPersonalListDao(appDatabase: AppDatabase): CommentPersonalListDao {
        return appDatabase.commentPersonalListDao()
    }

    @Provides
    fun provideCommentPersonalListRepository(commentPersonalListDao: CommentPersonalListDao): CommentPersonalListRepository {
        return CommentPersonalListRepositoryImpl(commentPersonalListDao)
    }

    // endregion


}
