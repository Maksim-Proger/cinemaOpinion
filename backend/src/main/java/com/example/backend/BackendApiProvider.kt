package com.example.backend

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BackendApiProvider {
    private const val BASE_URL = "http://147.45.233.103/"
    private const val BACKEND_HOST = "147.45.233.103"
    private val API_KEY = BuildConfig.API_SECRET_KEY

    fun avatarUrl(userId: String): String = "${BASE_URL}avatars/$userId"

    val imageHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val requestWithKey = if (request.url.host == BACKEND_HOST) {
                    request.newBuilder()
                        .addHeader("X-API-Key", API_KEY)
                        .build()
                } else {
                    request
                }
                chain.proceed(requestWithKey)
            }
            .build()
    }

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
