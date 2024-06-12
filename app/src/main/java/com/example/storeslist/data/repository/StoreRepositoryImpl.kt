package com.example.storeslist.data.repository

import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val remoteDataSource: FrogmiRemoteDataSource
) : StoreRepository {
    override fun getStores(perPage: Int, page: Int): Flow<List<Store>> {
        return flow {
            emit(remoteDataSource.getStores(perPage, page))
        }
    }
}