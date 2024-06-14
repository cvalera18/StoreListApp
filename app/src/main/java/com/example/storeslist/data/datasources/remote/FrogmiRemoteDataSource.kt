package com.example.storeslist.data.datasources.remote

import com.example.storeslist.data.model.StoreResponse

interface FrogmiRemoteDataSource {
    suspend fun getInitialStores(): StoreResponse
    suspend fun getNextStorePage(nextPage: String): StoreResponse

}