package com.pozmaxpav.cinemaopinion.di

import android.content.Context
import com.pozmaxpav.cinemaopinion.data.api.MovieListApi
import com.pozmaxpav.cinemaopinion.data.repository.MovieRepositoryImpl
import com.pozmaxpav.cinemaopinion.data.repository.SharedPreferencesRepository
import com.pozmaxpav.cinemaopinion.domain.repository.MovieRepository
import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository
import com.pozmaxpav.cinemaopinion.domain.usecase.GetModeActivationSystemTheme
import com.pozmaxpav.cinemaopinion.domain.usecase.GetModeApplicationThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.SaveModeActivationSystemThemeUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.SaveModeApplicationThemeUseCase
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

}