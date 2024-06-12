package com.example.storeslist.di

import com.example.storeslist.data.network.FrogmiApiService
import com.example.storeslist.data.network.HttpErrorInterceptor
import com.example.storeslist.data.network.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFrogmiApiService(): FrogmiApiService {
        return RetrofitInstance.api
    }
}