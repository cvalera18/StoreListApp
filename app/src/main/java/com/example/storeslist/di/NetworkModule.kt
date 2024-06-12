package com.example.storeslist.di

import com.example.storeslist.network.FrogmiApiService
import com.example.storeslist.network.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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