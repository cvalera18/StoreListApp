package com.example.storeslist.data.datasources.remote

import com.example.storeslist.data.model.StoreResponse
import com.example.storeslist.domain.model.Store

interface FrogmiRemoteDataSource {
    suspend fun getInitialStores(perPage: Int, page: Int): StoreResponse
    suspend fun getNextStorePage(nextPage: String): StoreResponse

}