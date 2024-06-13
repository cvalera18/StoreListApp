package com.example.storeslist.data.repository

import com.example.storeslist.data.datasources.local.LocalDataSource
import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
import com.example.storeslist.data.network.NetworkUtils
import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val remoteDataSource: FrogmiRemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val networkUtils: NetworkUtils
) : StoreRepository {
    override fun getStores(perPage: Int, page: Int): Flow<List<Store>> {
        return flow {
            if (networkUtils.isInternetAvailable()) {
                val remoteStores = remoteDataSource.getStores(perPage, page)
                localDataSource.saveStores(remoteStores)
                emit(remoteStores)
            } else {
                emitAll(localDataSource.getStores())
            }
        }
    }
}