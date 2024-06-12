package com.example.storeslist.di

import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindRemoteDataSource(
        frogmiRemoteDataSourceImpl: FrogmiRemoteDataSourceImpl
    ): FrogmiRemoteDataSource
}