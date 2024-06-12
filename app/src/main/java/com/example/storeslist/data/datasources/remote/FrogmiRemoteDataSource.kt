package com.example.storeslist.data.datasources.remote

import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.model.StoreResponse

interface FrogmiRemoteDataSource {
    suspend fun getStores(perPage: Int, page: Int): List<Store>
}