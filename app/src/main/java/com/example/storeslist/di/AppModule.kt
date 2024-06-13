package com.example.storeslist.di

import android.content.Context
import com.example.storeslist.data.datasources.local.LocalDataSource
import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
import com.example.storeslist.data.network.NetworkUtils
import com.example.storeslist.data.repository.StoreRepositoryImpl
import com.example.storeslist.domain.repository.StoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideStoreRepository(
        remoteDataSource: FrogmiRemoteDataSource,
        localDataSource: LocalDataSource,
        networkUtils: NetworkUtils
    ): StoreRepository {
        return StoreRepositoryImpl(remoteDataSource, localDataSource, networkUtils)
    }

    @Provides
    @Singleton
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}