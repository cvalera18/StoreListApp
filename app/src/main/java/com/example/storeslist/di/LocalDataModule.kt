package com.example.storeslist.di

import com.example.storeslist.data.datasources.local.LocalDataSource
import com.example.storeslist.data.datasources.local.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    fun provideStoreLocalDataSource(realm: Realm): LocalDataSource {
        return LocalDataSourceImpl(realm)
    }
}