package com.pozmaxpav.cinemaopinion.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pozmaxpav.cinemaopinion.data.api.MovieListApi
import com.pozmaxpav.cinemaopinion.data.localdb.appdb.AppDatabase
import com.pozmaxpav.cinemaopinion.data.localdb.dao.SelectedMovieDao
import com.pozmaxpav.cinemaopinion.data.localdb.dao.UserDao
import com.pozmaxpav.cinemaopinion.data.repository.MovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.SelectedMovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.SharedPreferencesRepository
import com.pozmaxpav.cinemaopinion.data.repository.UserRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase.FirebaseRepositoryImpl
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.SelectedMovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository
import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.RemoveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.SaveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.GetFilmByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.GetListSelectedFilmsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.InsertFilmUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.GetModeActivationSystemTheme
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.GetModeApplicationThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.GetUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.InsertUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.SaveModeActivationSystemThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.theme.SaveModeApplicationThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.UpdateUserUseCase
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
    fun provideThemeRepository(@ApplicationContext context: Context): ThemeRepository {
        return SharedPreferencesRepository(context)
    }

    @Provides
    fun provideSaveModeApplicationThemeUseCase(themeRepository: ThemeRepository): SaveModeApplicationThemeUseCase {
        return SaveModeApplicationThemeUseCase(themeRepository)
    }

    @Provides
    fun provideGetModeApplicationThemeUseCase(themeRepository: ThemeRepository): GetModeApplicationThemeUseCase {
        return GetModeApplicationThemeUseCase(themeRepository)
    }

    @Provides
    fun provideSaveModeActivationSystemThemeUseCase(themeRepository: ThemeRepository): SaveModeActivationSystemThemeUseCase {
        return SaveModeActivationSystemThemeUseCase(themeRepository)
    }

    @Provides
    fun provideGetModeActivationSystemTheme(themeRepository: ThemeRepository): GetModeActivationSystemTheme {
        return GetModeActivationSystemTheme(themeRepository)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "user_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideInsertUserUseCase(userRepository: UserRepository): InsertUserUseCase {
        return InsertUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserUseCase(userRepository: UserRepository): UpdateUserUseCase {
        return UpdateUserUseCase(userRepository)
    }


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

    @Provides
    @Singleton
    fun provideGetListSelectedFilmsUseCase(selectedMovieRepository: SelectedMovieRepository): GetListSelectedFilmsUseCase {
        return GetListSelectedFilmsUseCase(selectedMovieRepository)
    }

    @Provides
    @Singleton
    fun provideInsertFilmUseCase(selectedMovieRepository: SelectedMovieRepository): InsertFilmUseCase {
        return InsertFilmUseCase(selectedMovieRepository)
    }

    @Provides
    @Singleton
    fun provideGetFilmById(selectedMovieRepository: SelectedMovieRepository): GetFilmByIdUseCase {
        return GetFilmByIdUseCase(selectedMovieRepository)
    }

    // Firebase

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
    fun provideFilmRepository(databaseReference: DatabaseReference): FirebaseRepository {
        return FirebaseRepositoryImpl(databaseReference) // Передача DatabaseReference в репозиторий
    }

    @Provides
    @Singleton
    fun provideSaveMovieUseCase(firebaseRepository: FirebaseRepository): SaveMovieUseCase {
        return SaveMovieUseCase(firebaseRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveMovieUseCase(firebaseRepository: FirebaseRepository): RemoveMovieUseCase {
        return RemoveMovieUseCase(firebaseRepository)
    }

    @Provides
    @Singleton
    fun provideGetMovieUseCase(firebaseRepository: FirebaseRepository): GetMovieUseCase {
        return GetMovieUseCase(firebaseRepository)
    }
}