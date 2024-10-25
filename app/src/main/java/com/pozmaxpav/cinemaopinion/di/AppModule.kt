package com.pozmaxpav.cinemaopinion.di
// TODO: Почистить модуль
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
import com.pozmaxpav.cinemaopinion.data.localdatastore.LocalUserManagerImpl
import com.pozmaxpav.cinemaopinion.data.localdb.appdb.AppDatabase
import com.pozmaxpav.cinemaopinion.data.localdb.dao.SelectedMovieDao
import com.pozmaxpav.cinemaopinion.data.localdb.dao.SeriesControlDao
import com.pozmaxpav.cinemaopinion.data.localdb.dao.UserDao
import com.pozmaxpav.cinemaopinion.data.localdb.migration.DatabaseMigrations
import com.pozmaxpav.cinemaopinion.data.repository.GetMovieInformationApiRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.MovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.SelectedMovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.SeriesControlRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.SharedPreferencesRepository
import com.pozmaxpav.cinemaopinion.data.repository.UserRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase.FirebaseRepositoryImpl
import com.pozmaxpav.cinemaopinion.domain.repository.GetMovieInformationApiRepository
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.SelectedMovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.SeriesControlRepository
import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository
import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.RemoveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.SaveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.DeleteSelectedFilmUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.GetFilmByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.GetListSelectedFilmsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.selectedFilm.InsertFilmUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCDeleteMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCGetListMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCGetMovieByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCInsertUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.seriescontrol.SCUpdateMovieUseCase
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
    fun provideGetMovieInformationApiRepository(api: GetMovieInformationApi): GetMovieInformationApiRepository {
        return GetMovieInformationApiRepositoryImpl(api)
    }

    // endregion

    // region Theme

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

    // endregion

    // region Room

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "user_database"
        )
            .addMigrations(DatabaseMigrations.MIGRATION_1_2)
            .build()
    }

    // endregion

    // region User

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

    @Provides
    @Singleton
    fun provideDeleteSelectedFilmUseCase(selectedMovieRepository: SelectedMovieRepository): DeleteSelectedFilmUseCase {
        return DeleteSelectedFilmUseCase(selectedMovieRepository)
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

    // endregion

    // region IntroductionScreens

    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ) : LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    // endregion

    // region SeriesControl

    @Provides
    @Singleton
    fun providesSeriesControlDao(appDatabase: AppDatabase): SeriesControlDao {
        return appDatabase.seriesControlDao()
    }

    @Provides
    @Singleton
    fun providesSeriesControlRepository(seriesControlDao: SeriesControlDao): SeriesControlRepository {
        return SeriesControlRepositoryImpl(seriesControlDao)
    }

    @Provides
    @Singleton
    fun providesSCDeleteMovieUseCase(seriesControlRepository: SeriesControlRepository): SCDeleteMovieUseCase {
        return SCDeleteMovieUseCase(seriesControlRepository)
    }

    @Provides
    @Singleton
    fun providesSCGetListMoviesUseCase(seriesControlRepository: SeriesControlRepository): SCGetListMoviesUseCase {
        return SCGetListMoviesUseCase(seriesControlRepository)
    }

    @Provides
    @Singleton
    fun providesSCGetMovieByIdUseCase(seriesControlRepository: SeriesControlRepository): SCGetMovieByIdUseCase {
        return SCGetMovieByIdUseCase(seriesControlRepository)
    }

    @Provides
    @Singleton
    fun providesSCInsertUseCase(seriesControlRepository: SeriesControlRepository): SCInsertUseCase {
        return SCInsertUseCase(seriesControlRepository)
    }

    @Provides
    @Singleton
    fun providesSCUpdateMovieUseCase(seriesControlRepository: SeriesControlRepository): SCUpdateMovieUseCase {
        return SCUpdateMovieUseCase(seriesControlRepository)
    }

    // endregion
}
