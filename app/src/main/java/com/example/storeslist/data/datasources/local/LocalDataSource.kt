package com.example.storeslist.data.datasources.local

import com.example.storeslist.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getStores(): Flow<List<Store>>
    suspend fun saveStores(stores: List<Store>)
}