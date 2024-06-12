package com.example.storeslist.di

import com.example.storeslist.data.datasources.local.LocalDataSource
import com.example.storeslist.data.repository.StoreRepositoryImpl
import com.example.storeslist.domain.repository.StoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideStoreRepository(dataSource: LocalDataSource): StoreRepository {
        return StoreRepositoryImpl(dataSource)
    }
}