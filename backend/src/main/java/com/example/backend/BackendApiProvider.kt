package com.example.backend

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BackendApiProvider {
    private const val BASE_URL = "http://147.45.233.103/"
    private const val API_KEY = "08e076eafcb91b1a188e738ff9bbda9ab24abb87e4bd7e6ade296783742f6948"

    val api: BackendApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }

        val apiKeyInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-API-Key", API_KEY)
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApi::class.java)
    }
}
