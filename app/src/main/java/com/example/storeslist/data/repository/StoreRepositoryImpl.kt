package com.example.storeslist.data.repository

import com.example.storeslist.data.datasources.local.LocalDataSource
import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
import com.example.storeslist.data.model.Links
import com.example.storeslist.data.network.NetworkUtils
import com.example.storeslist.domain.mapper.toStore
import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class StoreRepositoryImpl(
    private val remoteDataSource: FrogmiRemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val networkUtils: NetworkUtils
) : StoreRepository {

    private val pages = MutableStateFlow<Links?>(null)
    override fun getStores(page: Int): Flow<List<Store>> {
        return flow {
            if (networkUtils.isInternetAvailable()) {
                try {
                    val currentPageInfo = pages.value

                    val remoteStores = if (currentPageInfo == null) { // First page
                        remoteDataSource.getInitialStores(10,1)
                    } else if (currentPageInfo.next != null) { // Have next page
                        remoteDataSource.getNextStorePage(currentPageInfo.next)
                    } else { // Last page
                        null
                    }
                    remoteStores?.let {response ->
                        pages.update {
                            response.links
                        }
                        val mappedStores = response.data.map { it.toStore() }
                        localDataSource.saveStores(mappedStores)
                        emit(mappedStores)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitAll(localDataSource.getStores())
                }
            } else {
                emitAll(localDataSource.getStores())
            }
        }
    }
}