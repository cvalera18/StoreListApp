package com.example.storeslist.domain.repository

import com.example.storeslist.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    val localStore: Flow<List<Store>>
    suspend fun getStores()
}