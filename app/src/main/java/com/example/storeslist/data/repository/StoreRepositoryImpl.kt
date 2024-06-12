package com.example.storeslist.data.repository

import com.example.storeslist.data.datasources.local.LocalDataSource
import com.example.storeslist.domain.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : StoreRepository {

    override val allStores = localDataSource.allStores

}