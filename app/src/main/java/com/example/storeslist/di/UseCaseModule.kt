package com.example.storeslist.di

import com.example.storeslist.domain.repository.StoreRepository
import com.example.storeslist.domain.usecases.GetLocalStoresUseCase
import com.example.storeslist.domain.usecases.GetStoresUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetStoresUseCase(storeRepository: StoreRepository): GetStoresUseCase {
        return GetStoresUseCase(storeRepository)
    }

    @Provides
    @Singleton
    fun provideGetLocalStoresUseCase(storeRepository: StoreRepository): GetLocalStoresUseCase {
        return GetLocalStoresUseCase(storeRepository)
    }

}