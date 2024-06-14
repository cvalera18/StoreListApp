package com.example.storeslist.data.repository

import com.example.storeslist.data.datasources.local.LocalDataSource
import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
import com.example.storeslist.data.model.Links
import com.example.storeslist.data.network.NetworkUtils
import com.example.storeslist.domain.mapper.toStore
import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.repository.StoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class StoreRepositoryImpl(
    private val remoteDataSource: FrogmiRemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val networkUtils: NetworkUtils
) : StoreRepository {

    private val pages = MutableStateFlow<Links?>(null)
    override val localStore: Flow<List<Store>> = localDataSource.getStores()
    override suspend fun getStores() {
        withContext(Dispatchers.IO) {
            if (networkUtils.isInternetAvailable()) {
                try {
                    val currentPageInfo = pages.value

                    val remoteStores = when {
                        currentPageInfo == null -> remoteDataSource.getInitialStores(10, 1) // First Page
                        currentPageInfo.next != null -> remoteDataSource.getNextStorePage(currentPageInfo.next) // Next page
                        else -> null // Last page
                    }
                    remoteStores?.let {response ->
                        pages.update {
                            response.links
                        }
                        val mappedStores = response.data.map { it.toStore() }
                        localDataSource.saveStores(mappedStores)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw e
                }
            } else {
                throw Exception("No internet connection")
            }
        }

    }
}