package com.example.storeslist.di

import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSourceImpl
import com.example.storeslist.data.network.FrogmiApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    fun providesRemoteDataSource(
        apiService: FrogmiApiService
    ): FrogmiRemoteDataSource {
        return FrogmiRemoteDataSourceImpl(apiService)
    }
}