package com.example.storeslist.di

import com.example.storeslist.data.network.FrogmiApiService
import com.example.storeslist.data.network.HttpErrorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.frogmi.com/api/v3/"
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFrogmiApiService(): FrogmiApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(HttpErrorInterceptor())
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FrogmiApiService::class.java)
    }
}